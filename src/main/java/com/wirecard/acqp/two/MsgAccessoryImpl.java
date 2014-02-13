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

import bsh.This;

/**
 * @author Wirecard AG (c) 2014. All rights reserved.
 * 
 */
public final class MsgAccessoryImpl { // implements IMsgAccessory {
	private static Logger logger = LoggerFactory
			.getLogger(MsgAccessoryImpl.class);

	private MsgAccessoryImpl() {
		// nothing - Utility classes should not have a public or default
		// constructor.
	}

	/**
	 * Utility AccessMethod for requesting a specific FieldValue
	 * 
	 * @param twoInput
	 *            the HexString of an ISO8583 InterchangeMsg
	 * @param cardSchemeType
	 *            the CardScheme allowed Values VISA, MASTERCARD, JCB
	 * @param fieldPath
	 *            requested FieldPath e. g. 30, could include Subfields e. g.
	 *            62.2
	 * @return the requested fieldvalue as String
	 * @throws ISOException
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 * @throws UnsupportedEncodingException
	 * 
	 */
	public static String readFieldValue(String twoInput, String cardSchemeType,
			String fieldPath) throws ISOException, UnsupportedEncodingException {
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

			if (classLoader == null) {
			    classLoader = Class.class.getClassLoader();
			}
			
			if (twoInput == null || twoInput.length() < 200)
				throw new IllegalArgumentException("TwoInput to short");

			GenericPackager sPackager = new GenericPackager(classLoader.getResourceAsStream(CardScheme
					.getCardScheme(cardSchemeType).getPath()));

			// logging
			logger.debug("used TWOInput : " + twoInput);

			ISOMsg isoMsg = new ISOMsg();

			isoMsg.setPackager(sPackager);

			if (logger.isTraceEnabled()) {
				org.jpos.util.Logger jPlogger = new org.jpos.util.Logger();
				jPlogger.addListener(new org.jpos.util.SimpleLogListener(
						MsgUtils.createLoggingProxy()));
				((org.jpos.util.LogSource) sPackager).setLogger(jPlogger,
						"debug");

			}

			switch (CardScheme.getCardScheme(cardSchemeType)) {
			case VISA:
				String visaDataPartHex = twoInput.substring(44,
						twoInput.length());
				// convert Hex to ASCII
				AsciiHexInterpreter asciiIn = new AsciiHexInterpreter();
				byte[] dataPart = asciiIn.uninterpret(
						visaDataPartHex.getBytes(), 0,
						visaDataPartHex.length() / 2);
				isoMsg.unpack(dataPart);
				break;
			case MASTERCARD:
				isoMsg.unpack(MsgUtils.getBytesFromTwoDataMC(twoInput));
				break;
			case JCB:
				throw new NotYetImpementedException();
				// isoMsg.unpack(MsgUtils.getBytesFromTwoDataJCB(twoInputTemp));
				// break;

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
			logger.error("error in ", e);
		}
		return null;

	}

	// MsgUtils.logISOMsgPlainText(isoMsg);

}
