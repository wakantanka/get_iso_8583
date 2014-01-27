package de.wirecard.acqp.twi;

import static org.junit.Assert.fail;

import java.io.UnsupportedEncodingException;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.header.BASE1Header;
import org.jpos.iso.packager.Base1Packager;
import org.jpos.iso.packager.GenericPackager;
import org.jpos.util.LogSource;
import org.jpos.util.Logger;
import org.jpos.util.SimpleLogListener;
import org.junit.Test;

import com.sleepycat.je.utilint.BitMap;

public class ParseISOMessageBase1VisaPackagerTest {

	@Test
	public void testParseAuthRequest() throws 
			UnsupportedEncodingException {
		GenericPackager packager = null;
		try {
		 packager = new GenericPackager(
				"src/main/resources/WDTbase1.xml");
//			packager = new GenericPackager(
//					"src/main/resources/base1.xml");
		
//		 Base1Packager packager2 = new Base1Packager();

		// TranID 18842255
		 String twoInput = "16010200B300000079542500000000000000000000000100F224648108E08012000000000000000410412435FFFFFF0019000000000000033333012308265160121415111711035601200006450476F4F0F2F3F0F8F6F0F1F2F1F485F0F0F0F0F0F0F1F1F0F0F585F0F0F0F0F0F0F1404040C9D6C240E2889699A340D58194854040404040404040404040C9D6C240E2889699A340D39683C9D5097801090580000000000E0040000000000000F1F140C6C6C6";
		
		String header = new String(twoInput.substring(0, 44));
		System.out.println("header #################################### "
				+ header);
		header = header + "00000000"; // spec says 52 Bytes

		
		String dataPart0 = new String(twoInput.substring(80, 158));
		System.out.println("preDataPart #################################### "
				+ dataPart0);

		String dataPart1 = new String(MsgUtils.decodeNibbleHex(twoInput
				.substring(158, 194)), "Cp1047");
		System.out.println("dataPart #################################### "
				+ dataPart1);

		String dataPart2 = new String(MsgUtils.decodeNibbleHex(twoInput
				.substring(194, 308)), "Cp1047");
		System.out.println("dataPart2 #################################### "
				+ dataPart2);

		String dataPart3 = new String(twoInput.substring(308, 346));
		System.out.println("dataPart3 #################################### "
				+ dataPart3);

		String dataPart4 = new String(MsgUtils.decodeNibbleHex(twoInput
				.substring(346, twoInput.length())), "Cp1047");
		System.out.println("dataPart4 #################################### "
				+ dataPart4);

		
		
		String dataPartAtlernativ = new String(twoInput.substring(44, twoInput.length()));
		System.out.println("dataPartAtlernativ #################################### "
				+ dataPartAtlernativ);
		
		StringBuilder sb = new StringBuilder();
		sb.append(dataPart0);
		sb.append(dataPart1);
		sb.append(dataPart2);
		sb.append(dataPart3);
		sb.append(dataPart4);

		Logger logger = new Logger();
		logger.addListener(new SimpleLogListener(System.out));
		((LogSource) packager).setLogger(logger, "debug");

		System.out.println("TWOInput : " + twoInput);

//		BASE1Header bASE1Header = new BASE1Header();
//		bASE1Header.unpack(header.getBytes());

		// Create ISO Message
		ISOMsg isoMsg = new ISOMsg();
//		isoMsg.setPackager(packager2);
		isoMsg.setPackager(packager);
		isoMsg.setHeader(("16010200B300000079542500000000000000000000000").getBytes());
//		isoMsg.unpack(twoInput.getBytes());
//		isoMsg.unpack(dataPartAtlernativ.getBytes("Cp1047"));
//		isoMsg.unpack(dataPartAtlernativ.getBytes());
		String dataPartAtlernativDecoded = new String(MsgUtils.stripAllFs(dataPartAtlernativ,20));
		System.out.println("dataPartAtlernativ_stripped #################################### " + dataPartAtlernativDecoded);
		isoMsg.unpack(dataPartAtlernativDecoded.getBytes());
		
//		MsgUtils.logISOHeader(bASE1Header);
		MsgUtils.logISOMsg(isoMsg);
		
//		 RandomAccess
		 String field = isoMsg.getMTI();
//		 System.out.println("48.43=" + field.toString());
		// assertEquals("SubField 48.43 not read correct",
		// "jIbyd6TeahmkABEAAAFrQmyXwm0=", isoMsg.getString("48.43"));
		//
		// // read PAN
		// assertEquals("Field 2 not read correct", "5405620000000000014",
		// isoMsg
		// .getComponent(2).getValue().toString());
		} catch (ISOException e) {
			e.printStackTrace();
			fail();
		}
	}
}
