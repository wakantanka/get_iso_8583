package com.wirecard.acqp.two;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.xml.DOMConfigurator;
import org.jpos.iso.AsciiHexInterpreter;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOHeader;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.header.BASE1Header;
import org.jpos.iso.packager.GenericPackager;
import org.junit.BeforeClass;
import org.junit.Test;

public class ParseISOMessageBase1VisaPackerTest {
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DOMConfigurator.configure( "resources/log4j.xml");
	}

	@Test
	public void testParseAuthRequest() throws 
			UnsupportedEncodingException {
		GenericPackager packager = null;
		try {
		 packager = new GenericPackager(
					"src/main/resources/base1.xml");
		

		// TranID 18842255
		 String twoInput = "16010200B300000079542500000000000000000000000100F224648108E08012000000000000000410412435FFFFFF0019000000000000033333012308265160121415111711035601200006450476F4F0F2F3F0F8F6F0F1F2F1F485F0F0F0F0F0F0F1F1F0F0F585F0F0F0F0F0F0F1404040C9D6C240E2889699A340D58194854040404040404040404040C9D6C240E2889699A340D39683C9D5097801090580000000000E0040000000000000F1F140C6C6C6";
		
		String header = new String(twoInput.substring(0, 44));
		System.out.println("header #################################### "
				+ header);

		
		String dataPartAtlernativ = new String(twoInput.substring(44, twoInput.length()));
		System.out.println("dataPartAtlernativ #################################### "
				+ dataPartAtlernativ);
		
//		Logger logger = new Logger();
//		logger.addListener(new SimpleLogListener(System.out));
//		((LogSource) packager).setLogger(logger, "debug");

		System.out.println("TWOInput : " + twoInput);

		AsciiHexInterpreter asciiIn = new AsciiHexInterpreter();
		// ISO Header
		BASE1Header bASE1Header = new BASE1Header();
		bASE1Header.unpack(asciiIn.uninterpret(header.getBytes(), 0, header.length()/2));

		// pharse ISO Message
		ISOMsg isoMsg = new ISOMsg();
		
		isoMsg.setHeader(bASE1Header);
		
		isoMsg.setPackager(packager);
		byte[] dataPartAtlernativBin = asciiIn.uninterpret(dataPartAtlernativ.getBytes(), 0, dataPartAtlernativ.length()/2);
		isoMsg.unpack(dataPartAtlernativBin);
		
//		MsgUtils.logISOHeader(bASE1Header);
		ISOHeader isoHeader = isoMsg.getISOHeader();
		String headerDump = ISOUtil.hexString(isoHeader.pack());
		System.out.println(("request header " + headerDump));
//		MsgUtils.logISOHeader(headerDump);
		
//		MsgUtils.logISOMsgPlainText(isoMsg);
		
//		 RandomAccess
		 String field = isoMsg.getMTI();
		 System.out.println("MTI=" + field.toString());
		 assertEquals("SubField 126.10 not read correct",
		 "11 FFF", isoMsg.getString("126.10"));
		 
		 
//		 byte [] field60 =(byte [])  isoMsg.getValue(60);
//		 System.out.println(	 Hex.encodeHexString( field60));
//		 assertEquals("Field 60 not read correct",
//				 "09",   Hex.encodeHexString( field60));
		 
//		 byte [] field63 =(byte [])  isoMsg.getValue(60);
//		 System.out.println(	 Hex.encodeHexString( field60));
		 
		 
		} catch (ISOException e) {
			e.printStackTrace();
			fail();
		}
	}
}
