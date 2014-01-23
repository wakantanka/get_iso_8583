package de.wirecard.acqp.twi;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.jpos.iso.BCDInterpreter;
import org.jpos.iso.ISOException;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SplitMsgPartsTest {

	private static String testdata;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		StringBuilder sb = new StringBuilder();

		File testdataFile = new File("./src/test/resources/testdata.ac");
		FileInputStream in = new FileInputStream(testdataFile);
		InputStreamReader iSr = new InputStreamReader(in, "UTF-8");

		char[] chars = new char[4 * 1024];
		int len;
		while ((len = iSr.read(chars)) >= 0) {
			sb.append(chars, 0, len);
		}
		iSr.close();
		testdata = sb.toString();
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSeperateGetMTI() {
		// assertEquals("F0F1F0F0", MsgUtils.GetMTI(testdata));
		assertEquals("0100", MsgUtils.GetMTI(testdata));
	}

	@Test
	public void testSeperateBitMap() throws DecoderException {
		assertEquals("723C440188E18008", MsgUtils.GetBitMap(testdata));
		//
		// char[] chars = "723C440188E18008".toCharArray();
		// byte[] bytes = Hex.decodeHex(chars);
		// System.out.print(BinaryCodec.toAsciiString(bytes));

		// for (int i = 0; i < bytes.length; i++) {
		// System.out.print(bytes[i]);
		// }
	}

	@Test
	public void testBinBitmap() throws ISOException {
		assertEquals("01110010", MsgUtils.hexToBinary("72"));

		assertEquals("0111001000111100",
				MsgUtils.hexStringToBinaryString("723C"));

		assertEquals(
				"0111001000111100010001000000000110001000111000011000000000001000",
				MsgUtils.hexStringToBinaryString("723C440188E18008"));

		// char[] bitmapBin =
		// MsgUtils.hexStringToBinaryString("723C440188E18008")
		// .toCharArray();
		// for (int i = 0; i < bitmapBin.length; i++) {
		// if (bitmapBin[i] == '1')
		// System.out.println(i + 1);
		// }

	}

	@Test
	public void testSeperateDataBlock() {
		// assertEqtuals("F0F1F0F0", MsgUtils.GetMTI(testdata));
		// System.out.println( MsgUtils.GetData(testdata));
		System.out.println(MsgUtils.stripFs("F0F0F0F0F0F0F1"));

	}

	@Test
	public void testStripFs() {
		assertEquals("01100", MsgUtils.stripFs("F0F1F1F0F0"));

	}

	@Test(expected = NumberFormatException.class)
	public void testStripFsNeg() {
		MsgUtils.stripFs("F0F181F0F0");

	}

	@Test
	public void testSplitHoleHexString() {
		assertEquals(
				"0100" + "723C4401F8E18008"
						+ "19540562000000000001",
				MsgUtils.cleanNibblesFromHexMsg("F0F1F0F0723C4401F8E18008F1F9F5F4F0F5F6F2F0F0F0F0F0F0F0F0F0F0F0F1"));

		

		System.out.println(MsgUtils.cleanNibblesFromHexMsg("F0F1F0F0723C440188E18008F1F9F5F4F0F5F6F2F0F0F0F0F0F0F0F0F0F0F0F1F4F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F5F0F5F0F1F2F0F1F3F0F3F0F9F5F8F9F2F7F8F1F3F0F3F0F9F0F1F2F0F1F5F1F1F5F4F1F1F8F1F2F0F6F0F1F3F4F0F1F0F6F2F0F0F3F5F0F0F1F2F0F1F4F5F4F9F3F5F482F0F0F0F0F0F0F1D9C5E3D382F0F0F0F0F0F0F1404040C3C3C240E3F140E28899A340D581948540404040404040C3C3C240E3F140E28899A340D340D7C1D5F0F6F0E3F6F1F0F5F0F0F0F0F1F9F2F0F35C5C5CF4F2F0F7F0F1F0F3F2F1F2F4F3F2F891C982A884F6E38581889492C1C2C5C1C1C1C699D894A8E7A694F07EF9F7F8F0F2F1F1F0F2F5F1F0F0F0F0F6F0F0F0F5F9F1D7C1D5F1F2"));
	System.out.println(MsgUtils.stripAllFs("F0F1F0F0723C440188E18008F1F9F5F4F0F5F6F2F0F0F0F0F0F0F0F0F0F0F0F1F4F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F5F0F5F0F1F2F0F1F3F0F3F0F9F5F8F9F2F7F8F1F3F0F3F0F9F0F1F2F0F1F5F1F1F5F4F1F1F8F1F2F0F6F0F1F3F4F0F1F0F6F2F0F0F3F5F0F0F1F2F0F1F4F5F4F9F3F5F482F0F0F0F0F0F0F1D9C5E3D382F0F0F0F0F0F0F1404040C3C3C240E3F140E28899A340D581948540404040404040C3C3C240E3F140E28899A340D340D7C1D5F0F6F0E3F6F1F0F5F0F0F0F0F1F9F2F0F35C5C5CF4F2F0F7F0F1F0F3F2F1F2F4F3F2F891C982A884F6E38581889492C1C2C5C1C1C1C699D894A8E7A694F07EF9F7F8F0F2F1F1F0F2F5F1F0F0F0F0F6F0F0F0F5F9F1D7C1D5F1F2"));

	
	}
	
	@Test
	public void testDecodeNibbleHex() throws DecoderException {
//		byte[] bytes  = MsgUtils.decodeNibbleHex("F0F1F0F0723C440188E18008F1F9F5F4F0F5F6F2F0F0F0F0F0F0F0F0F0F0F0F1F4F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F5F0F5F0F1F2F0F1F3F0F3F0F9F5F8F9F2F7F8F1F3F0F3F0F9F0F1F2F0F1F5F1F1F5F4F1F1F8F1F2F0F6F0F1F3F4F0F1F0F6F2F0F0F3F5F0F0F1F2F0F1F4F5F4F9F3F5F482F0F0F0F0F0F0F1D9C5E3D382F0F0F0F0F0F0F1404040C3C3C240E3F140E28899A340D581948540404040404040C3C3C240E3F140E28899A340D340D7C1D5F0F6F0E3F6F1F0F5F0F0F0F0F1F9F2F0F35C5C5CF4F2F0F7F0F1F0F3F2F1F2F4F3F2F891C982A884F6E38581889492C1C2C5C1C1C1C699D894A8E7A694F07EF9F7F8F0F2F1F1F0F2F5F1F0F0F0F0F6F0F0F0F5F9F1D7C1D5F1F2");
		byte[] bytes = StringUtils.getBytesUtf8("F0F1F0F0723C440188E18008F1F9F5F4F0F5F6F2F0F0F0F0F0F0F0F0F0F0F0F1F4F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F5F0F5F0F1F2F0F1F3F0F3F0F9F5F8F9F2F7F8F1F3F0F3F0F9F0F1F2F0F1F5F1F1F5F4F1F1F8F1F2F0F6F0F1F3F4F0F1F0F6F2F0F0F3F5F0F0F1F2F0F1F4F5F4F9F3F5F482F0F0F0F0F0F0F1D9C5E3D382F0F0F0F0F0F0F1404040C3C3C240E3F140E28899A340D581948540404040404040C3C3C240E3F140E28899A340D340D7C1D5F0F6F0E3F6F1F0F5F0F0F0F0F1F9F2F0F35C5C5CF4F2F0F7F0F1F0F3F2F1F2F4F3F2F891C982A884F6E38581889492C1C2C5C1C1C1C699D894A8E7A694F07EF9F7F8F0F2F1F1F0F2F5F1F0F0F0F0F6F0F0F0F5F9F1D7C1D5F1F2");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
//			Charbytes[i]
			System.out.println(i +" = " +  bytes[i]);
//			   sb.append(String.format("%02X", bytes[i]));
		} 
		Hex hexer = new Hex();
		byte[] bytesDec  = hexer.decode( bytes );
		for (int i = 0; i < bytesDec.length; i++) {
//			System.out.println(bytes[i]);
			   sb.append(String.format("%01X", bytesDec[i]));
			   
		} 
		System.out.println("encoHex: "  + Hex.encodeHexString(bytesDec).length());
		    System.out.println(sb.toString());
//		assertEquals("01100", MsgUtils.decodeNibbleHex("F0F1F0F0723C440188E18008F1F9F5F4F0F5F6F2F0F0F0F0F0F0F0F0F0F0F0F1F4F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F5F0F5F0F1F2F0F1F3F0F3F0F9F5F8F9F2F7F8F1F3F0F3F0F9F0F1F2F0F1F5F1F1F5F4F1F1F8F1F2F0F6F0F1F3F4F0F1F0F6F2F0F0F3F5F0F0F1F2F0F1F4F5F4F9F3F5F482F0F0F0F0F0F0F1D9C5E3D382F0F0F0F0F0F0F1404040C3C3C240E3F140E28899A340D581948540404040404040C3C3C240E3F140E28899A340D340D7C1D5F0F6F0E3F6F1F0F5F0F0F0F0F1F9F2F0F35C5C5CF4F2F0F7F0F1F0F3F2F1F2F4F3F2F891C982A884F6E38581889492C1C2C5C1C1C1C699D894A8E7A694F07EF9F7F8F0F2F1F1F0F2F5F1F0F0F0F0F6F0F0F0F5F9F1D7C1D5F1F2"));
		
	}

	@Test
	public void testAuslesenFeld43Struk() throws DecoderException {
//		 System.out.println(MsgUtils.hextoASCII("001404040C3C3C240E3F140E28899A340D581948"));
//		 System.out.println(MsgUtils.hextoASCII("1D9C5E3D3"));
		 byte[] bytes = Hex.decodeHex("D9C5E3D3".toCharArray());
//		 /52:45:54:4c
		 StringBuffer result = new StringBuffer();
		 for (byte b : bytes) {
		     result.append((char) b + 12);
		     result.append(" "); // delimiter
		 }
		 System.out.println("result " + result.toString() );
		 System.out.println(new String(bytes));
	
			byte [] byteArray = {0x41,0x42,0x43,0x62,0x47};
			
			System.out.println("ASCII Equivalent of Values in Byte Array:\n");

			System.out.println(new String(byteArray));
			
			
			String string  = "11011100010000010001000000000000";
			String string1 = "00000000010000110000100000101100";

			System.out.println(new BigInteger(string1, 2).toString(16));
			System.out.println(new BigInteger(string, 2).toString(16));
			System.out.println(Long.toHexString(Long.parseLong(string1,2)));
			System.out.println(Long.toHexString(Long.parseLong(string,2)));
			
			
//			82 --> 62
//			http://en.wikipedia.org/wiki/EBCDIC
			System.out.println();
			System.out.println((char) 62);
			System.out.println((char) 129);
			
			
//			BCDInterpreter.LEFT_PADDED_F
		    }
	
	@Test
	public void testDecodeEBCDIC() throws UnsupportedEncodingException {
		
//		System.out.println(new String(MsgUtils.decodeNibbleHex("820000001") ,   "Cp1047"));
//		System.out.println(new String(MsgUtils.decodeNibbleHex("82F0F0F0F0F0F0F1") ,   "Cp1047"));
		assertEquals(new String(MsgUtils.decodeNibbleHex("82F0F0F0F0F0F0F1"),   "Cp1047"), "b0000001");

//		System.out.println(new String(MsgUtils.decodeNibbleHex("D9C5E3D382F0F0F0F0F0F0F1") ,   "Cp1047")); //RETLb0000001
		assertEquals(new String(MsgUtils.decodeNibbleHex("D9C5E3D382F0F0F0F0F0F0F1"),   "Cp1047"), "RETLb0000001");
		}	
	
	
}
