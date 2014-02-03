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
import org.apache.log4j.xml.DOMConfigurator;

/**
 * @author jan.wahler Copyright Wirecard AG (c) 2014. All rights reserved.
 * 
 */
public class MsgAccessoryImpl implements IMsgAccessory {
	private GenericPackager packager = null;
	private AsciiHexInterpreter asciiIn = new AsciiHexInterpreter();
	private String twoInput;
	private static Logger logger = LoggerFactory.getLogger(MsgAccessoryImpl.class);
	 
	private CardScheme scheme;

	public MsgAccessoryImpl() {
		super();
		DOMConfigurator.configure( "resources/log4j.xml");
	}

	public MsgAccessoryImpl(String twoInput, String cardSchemeType)
			throws ISOException {
		DOMConfigurator.configure( "resources/log4j.xml");
		this.twoInput = twoInput;
		scheme = CardScheme.getCardScheme(cardSchemeType);

		packager = new GenericPackager(scheme.getPath());

		// throw new NotYetImpementedException();

	}

	public String getFieldValue(String fieldPath) throws ISOException,
			UnsupportedEncodingException, IllegalStateException, NotYetImpementedException {
		if(twoInput==null)
			throw new IllegalStateException(
					"Called Method without TWOInputData. Use right constructor or utiltyMethod");
		else if (twoInput.length()< 200) {
			throw new IllegalArgumentException("TwoInput to short");
		}
		
		ISOMsg isoMsg = new ISOMsg();

		isoMsg.setPackager(packager);
		
		  logger.info("Hello World");
//		 org.jpos.util.Logger logger = new Logger();
//		 logger.addListener(new org.jpos.util.SimpleLogListener(System.out));
//		 ((org.jpos.util.LogSource) packager).setLogger(logger, "debug");


		switch (scheme) {
		case VISA:
			String visaDataPartHex = twoInput.substring(44, twoInput.length());
			//convert Hex to ASCII
			byte[] dataPart = asciiIn.uninterpret(visaDataPartHex.getBytes(), 0, visaDataPartHex.length() / 2);
			isoMsg.unpack(dataPart);
			break;
		case MASTERCARD:
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
			isoMsg.unpack(data.getBytes());

			break;
		case JCB:
			 throw new NotYetImpementedException();
		default:
			throw new IllegalStateException(
					"Can't detemine CardScheme. You schould never see this. Sorry!");

		}
		//@TODO sysout 
		System.out.println("TWOInput : " + twoInput);
		MsgUtils.logISOMsgPlainText(isoMsg);

		if (isoMsg.getValue(fieldPath) instanceof byte[]) {
			return Hex.encodeHexString(
					(byte[]) isoMsg.getComponent(fieldPath).getValue())
					.toString();
		} else {
			return isoMsg.getString(fieldPath);
		}

		// throw new NotYetImpementedException();

	}

	// TODO Exception wie behandeln?
	// utiltyMethod with full data
	public String getFieldValue(String twoInput, String cardSchemeType,
			String fieldPath) throws ISOException,
			UnsupportedEncodingException, IllegalStateException {
		
		this.twoInput = twoInput;
		// TODO absichern two imput mit final für construktur ok, aber nicht für Utilzugriff ??
		// evtl ergibt sich ein Problem wenn zuerst constructor aufruf twoInput setzt, dannach Verwednung mit diser utilMethode
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
