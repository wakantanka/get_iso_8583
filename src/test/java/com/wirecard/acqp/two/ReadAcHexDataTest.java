package com.wirecard.acqp.two;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.DecoderException;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ReadAcHexDataTest {
	private static String testdata;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
//		DOMConfigurator.configure( "resources/log4j.xml");

		
		StringBuilder sb = new StringBuilder();

		File testdataFile = new File("./src/test/resources/testdataMCAuthRequest.ac");
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
	public void testReadACFromRecources() throws IOException,
			FileNotFoundException, UnsupportedEncodingException, DecoderException {


		
		System.out.println(testdata);
		System.out.println();
//		hex.decode(decodeNibbleHex(testdata));
//		System.out.println((decodeNibbleHex(testdata)));
		byte[]  bytes = decodeNibbleHex(testdata);
		
		
		for (int i = 0; i < bytes.length; i++) {
			System.out.print(bytes[i]);
		}
	}
	
	private  byte[] decodeNibbleHex(String input)
	  {
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    char[] chars = input.toCharArray();
	    for (int i = 0; i < chars.length - 1; i += 2) {
	      char[] bChars = new char[2];
	      bChars[0] = chars[i];
	      bChars[1] = chars[(i + 1)];
	      int val = Integer.decode("0x" + new String(bChars)).intValue();
	      baos.write((byte)val);
	    }
	    return baos.toByteArray();
	  }
	

	@Test
	public void testDecodeNibbleHexToHex()
	{
		String result = decodeNibbleHexToBinary(this.testdata);
		System.out.println(result);
//		assertEquals("0100", result);
	}
	@Test
	public void testDecodeNibbleHexToBinary()
	{
		
		String testdata = "F0F1F0F0";
		String result = decodeNibbleHexToBinary(testdata);
		System.out.println(result);
		assertEquals("0100", result);
				
	}

	private String decodeNibbleHexToBinary(String input) {
		StringBuilder result = new StringBuilder() ;
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) != 'F' && i%2==1 )
				result.append(input.charAt(i));
		}
		return result.toString();
	}
	
}


