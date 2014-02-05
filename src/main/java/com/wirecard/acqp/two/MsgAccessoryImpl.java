/**
 * 
 */
package com.wirecard.acqp.two;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Hex;
import org.jpos.iso.AsciiHexInterpreter;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jan.wahler Copyright Wirecard AG (c) 2014. All rights reserved.
 * 
 */
public class MsgAccessoryImpl implements IMsgAccessory {
	private GenericPackager packager = null;
	private AsciiHexInterpreter asciiIn = new AsciiHexInterpreter();
	private final String twoInputFromConstructor;
	private String twoInputFromUtilMethod = null;
	private static Logger logger = LoggerFactory
			.getLogger(MsgAccessoryImpl.class);

	private CardScheme scheme;

	public MsgAccessoryImpl() {
		super();
		this.twoInputFromConstructor = null;
	}

	public MsgAccessoryImpl(String twoInput, String cardSchemeType)
			throws ISOException {
		this.twoInputFromConstructor = twoInput;
		scheme = CardScheme.getCardScheme(cardSchemeType);

		packager = new GenericPackager(scheme.getPath());

		// throw new NotYetImpementedException();

	}

	//TODO Threadsafty
	public String getFieldValue(String fieldPath) throws IllegalStateException, NotYetImpementedException, IllegalArgumentException, ISOException {
		try {
			String twoInputTemp;
			if (twoInputFromUtilMethod == null) {
				twoInputTemp = twoInputFromConstructor;
			}
			  else 
				  twoInputTemp = twoInputFromUtilMethod;
			
			if (twoInputTemp == null)
				throw new IllegalStateException(
						"Called Method without TWOInputData. Use right constructor or utiltyMethod");
			if (twoInputTemp.length() < 200)  
				throw new IllegalArgumentException("TwoInput to short");

			// logging
			logger.debug("used TWOInput : " + twoInputTemp);
			
				ISOMsg isoMsg = new ISOMsg();

			isoMsg.setPackager(packager);

			if (logger.isTraceEnabled()) {
				org.jpos.util.Logger jPlogger = new org.jpos.util.Logger();
				jPlogger.addListener(new org.jpos.util.SimpleLogListener(MsgUtils
						.createLoggingProxy()));
				((org.jpos.util.LogSource) packager).setLogger(jPlogger, "debug");

			}

			switch (scheme) {
			case VISA:
				String visaDataPartHex = twoInputTemp.substring(44, twoInputTemp.length());
				// convert Hex to ASCII
				byte[] dataPart = asciiIn.uninterpret(visaDataPartHex.getBytes(),
						0, visaDataPartHex.length() / 2);
				isoMsg.unpack(dataPart);
				break;
			case MASTERCARD:
				isoMsg.unpack(getBytesFromTwoDataMC(twoInputTemp));
				break;
			case JCB:
				isoMsg.unpack(getBytesFromTwoDataJCB(twoInputTemp));
				throw new NotYetImpementedException();
//				break;
				
			default:
				throw new IllegalStateException(
						"Can't determine CardScheme. You schould never see this. Sorry!");

			}
			// logging
			if (logger.isTraceEnabled())
				isoMsg.dump(MsgUtils.createLoggingProxy(), "");
			logger.debug(MsgUtils.getISOMsgPlainText(isoMsg));
			// MsgUtils.logISOMsgPlainText(isoMsg);

			if (isoMsg.getValue(fieldPath) instanceof byte[]) {
				return Hex.encodeHexString(
						(byte[]) isoMsg.getComponent(fieldPath).getValue())
						.toString();
			} else {
				return isoMsg.getString(fieldPath);
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("error in getFieldValue", e);
			return e.getMessage();
//		} catch (ISOException e) {
//			logger.error("error in getFieldValue" , e);
//			return e.getMessage();
		}finally {
			twoInputFromUtilMethod = null;
		
		}
		
	}

	private byte[] getBytesFromTwoDataMC(
			String twoInput)
					throws UnsupportedEncodingException, ISOException {
		String mti = new String(MsgUtils.decodeNibbleHex(twoInput
				.substring(0, 8)), "Cp1047");
		String bitmap = new String(MsgUtils.GetBitMap(twoInput));
		
		String dataPartMC = new String(MsgUtils.decodeNibbleHex(twoInput
				.substring(24, twoInput.length())), "Cp1047");
		
		StringBuilder sb = new StringBuilder();
		sb.append(mti);
		sb.append(bitmap);
		sb.append(dataPartMC);
		
		String data = sb.toString();
		return (data.getBytes());
	}
	private byte[] getBytesFromTwoDataJCB(
			String twoInput)
			throws UnsupportedEncodingException, ISOException {
		String mti = new String(MsgUtils.decodeNibbleHex(twoInput
				.substring(0, 8)), "Cp1047");
		String bitmap = new String(MsgUtils.GetBitMap(twoInput));

//		String dataPartJcb = new  String(twoInput				.substring(24, twoInput.length()) );
		String dataPartJcb =  new String(MsgUtils.stripAllFs(twoInput.substring(24, twoInput.length()),110));

		StringBuilder sb = new StringBuilder();
		sb.append(mti);
		sb.append(bitmap);
		sb.append(dataPartJcb);

		String data = sb.toString();
		return (data.getBytes());
	}

	// utiltyMethod with full data
	public String getFieldValue(String twoInput, String cardSchemeType,
			String fieldPath) throws ISOException,
			UnsupportedEncodingException, IllegalStateException {

		this.twoInputFromUtilMethod = twoInput;
		scheme = CardScheme.getCardScheme(cardSchemeType);

		packager = new GenericPackager(scheme.getPath());
		return getFieldValue(fieldPath);

	}

	@SuppressWarnings("serial")
	class NotYetImpementedException extends java.lang.RuntimeException {

		public NotYetImpementedException() {
			super("NotYetImpementedException");
		}

	};

}
