/**
 * 
 */
package com.wirecard.acqp.twi;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Hex;
import org.jpos.iso.AsciiHexInterpreter;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;
import org.jpos.util.LogSource;
import org.jpos.util.Logger;
import org.jpos.util.SimpleLogListener;

/**
 * @author jan.wahler Copyright Wirecard AG (c) 2014. All rights reserved.
 * 
 */
public class MsgAccessoryImpl implements IMsgAccessory {
	private GenericPackager packager = null;
	private AsciiHexInterpreter asciiIn = new AsciiHexInterpreter();
	private String twoInput;

	private CardScheme scheme;

	public MsgAccessoryImpl() {
		super();
	}

	public MsgAccessoryImpl(String twoInput, String cardSchemeType)
			throws ISOException {
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

		switch (scheme) {
		case VISA:
			byte[] dataPart = asciiIn.uninterpret(
					twoInput.substring(44, twoInput.length()).getBytes(), 0,
					twoInput.substring(44, twoInput.length()).length() / 2);
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
					"Can't dertemine CardScheme. You schould never see this. ");

		}
		System.out.println("TWOInput : " + twoInput);
		MsgUtils.logISOMsg(isoMsg);

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

		// Logger logger = new Logger();
		// logger.addListener(new SimpleLogListener(System.out));
		// ((LogSource) packager).setLogger(logger, "debug");

	}

	@SuppressWarnings("serial")
	class NotYetImpementedException extends java.lang.RuntimeException {

		public NotYetImpementedException() {
			super("NotYetImpementedException");
		}

	};

}
