package com.wirecard.acqp.two;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

import org.apache.log4j.xml.DOMConfigurator;
import org.jpos.iso.ISOException;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.wirecard.acqp.two.MsgAccessoryImpl.NotYetImpementedException;

public class ParserHardeningTest {
	private  IMsgAccessory msgAccessory = new MsgAccessoryImpl();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DOMConfigurator.configure("resources/log4j.xml");

	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testParserHardeningVisa() throws IOException {
		// http://chrismelinn.wordpress.com/2013/04/12/using-the-golden-master-technique-to-test-legacy-code/
		// for hardening parser
		
		File testdataFile = new File(
				"./src/test/resources/VISA-parser-hardening.txt");
		parseBulkFile(testdataFile, CardScheme.VISA);
		
	}
	

//	@Test (expected = NotYetImpementedException.class)
	public void testParserHardeningJCB() throws IOException {
		
		File testdataFile = new File(
				"./src/test/resources/JCB-parser-hardening.txt");
 		parseBulkFile(testdataFile, CardScheme.JCB);
		
	}
	
//	@Test
	public void testParserHardeningMC() throws IOException {

		File testdataFile = new File(
				"./src/test/resources/MasterCard-parser-hardening.txt");
		parseBulkFile(testdataFile, CardScheme.MASTERCARD);
	}

	private  void parseBulkFile(File testdatafile, CardScheme scheme) {

		if (!testdatafile.canRead() || !testdatafile.isFile())
			fail("can't read testdatafile.");

		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(testdatafile));
			String row = null;
			while ((row = in.readLine()) != null) {
				String[] segs = row.split(Pattern.quote(","));
				String twoData = segs[segs.length - 1].trim();
				
				if (!MsgUtils.isHex(twoData)) {
					System.out.println("WARNING : " + twoData
							+ "is not a hexString");
					continue;
				}
				System.out.println(twoData);
				
				 parseRow(twoData, scheme);

			}
		} catch (IOException e) {
			e.printStackTrace();
			fail("can't opem testdatafile.");
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					fail("can't close testdatafile.");
				}
		}
	}

	private  void parseRow(String twoData, CardScheme scheme) {
		try 
		{
			String pan = msgAccessory.getFieldValue(twoData, scheme.toString(), "2");
			System.out.println("PAN " + pan);
			assertNotNull("PAN is null" + pan);
			
		} catch (IllegalStateException e) {
			fail(e.getMessage());
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			fail(e.getMessage());
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			fail(e.getMessage());
			e.printStackTrace();
		} catch (ISOException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		
	}
}
