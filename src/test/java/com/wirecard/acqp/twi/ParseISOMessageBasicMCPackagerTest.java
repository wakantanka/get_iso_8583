package com.wirecard.acqp.twi;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;
import org.junit.Test;

public class ParseISOMessageBasicMCPackagerTest {

	@Test
	public void testParseAuthRequest() throws ISOException,
			UnsupportedEncodingException {
		GenericPackager packager = new GenericPackager("src/main/resources/basic.xml");

		// TranID 18749354
		String twoInput = "F0F1F0F0723C440188E18008F1F9F5F4F0F5F6F2F0F0F0F0F0F0F0F0F0F0F0F1F4F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F5F0F5F0F1F2F0F1F3F0F3F0F9F5F8F9F2F7F8F1F3F0F3F0F9F0F1F2F0F1F5F1F1F5F4F1F1F8F1F2F0F6F0F1F3F4F0F1F0F6F2F0F0F3F5F0F0F1F2F0F1F4F5F4F9F3F5F482F0F0F0F0F0F0F1D9C5E3D382F0F0F0F0F0F0F1404040C3C3C240E3F140E28899A340D581948540404040404040C3C3C240E3F140E28899A340D340D7C1D5F0F6F0E3F6F1F0F5F0F0F0F0F1F9F2F0F35C5C5CF4F2F0F7F0F1F0F3F2F1F2F4F3F2F891C982A884F6E38581889492C1C2C5C1C1C1C699D894A8E7A694F07EF9F7F8F0F2F1F1F0F2F5F1F0F0F0F0F6F0F0F0F5F9F1D7C1D5F1F2";

		String mti = new String(MsgUtils.decodeNibbleHex(twoInput.substring(0,
				8)), "Cp1047");
		String bitmap = new String(MsgUtils.GetBitMap(twoInput));

		String dataPart = new String(MsgUtils.decodeNibbleHex(twoInput
				.substring(24, twoInput.length())), "Cp1047");

		StringBuilder sb = new StringBuilder();
		sb.append(mti);
		sb.append(bitmap);
		sb.append(dataPart);

//		 Logger logger = new Logger();
//		 logger.addListener(new SimpleLogListener(System.out));
//		 ((LogSource) packager).setLogger(logger, "debug");

		String data = sb.toString();
		System.out.println("TWOInput : " + twoInput);
		System.out.println("DATA : " + data);

		// Create ISO Message
		ISOMsg isoMsg = new ISOMsg();
		isoMsg.setPackager(packager);
		isoMsg.unpack(data.getBytes());

		MsgUtils.logISOMsg(isoMsg);

		// read PAN
		assertEquals("could not read Field 2", "5405620000000000014", isoMsg
				.getComponent(2).getValue().toString());

		// RandomAccess
		assertEquals("SubField 48.43 not read correct",
				"jIbyd6TeahmkABEAAAFrQmyXwm0=", isoMsg.getString("48.43"));
		
		assertEquals("could not read Field 61", "1025100006000591PAN12", isoMsg
				.getComponent(61).getValue().toString());

	}

}
