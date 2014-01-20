package de.wirecard.acqp.twi;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.BitSet;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.BinaryCodec;
import org.apache.commons.codec.binary.Hex;
import org.jpos.iso.ISOException;
import org.jpos.util.FrozenLogEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SplitMsgPartsTest {

	private static String testdata;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		StringBuilder sb = new StringBuilder();

		File testdataFile = new File("./resources/testdata.ac");
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

		
//		char[] bitmapBin = MsgUtils.hexStringToBinaryString("723C440188E18008")
//				.toCharArray();
//		for (int i = 0; i < bitmapBin.length; i++) {
//			if (bitmapBin[i] == '1')
//				System.out.println(i + 1);
//		}
		
	}

		@Test
		public void testSeperateDataBlock() {
			// assertEqtuals("F0F1F0F0", MsgUtils.GetMTI(testdata));
//			System.out.println( MsgUtils.GetData(testdata));	
			System.out.println( MsgUtils.stripFs("F0F0F0F0F0F0F1"));	

	}

}
