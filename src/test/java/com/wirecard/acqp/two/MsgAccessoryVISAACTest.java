/**
 * 
 */
package com.wirecard.acqp.two;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;

import org.apache.log4j.xml.DOMConfigurator;
import org.jpos.iso.ISOException;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author jan.wahler
 * 
 * 
 */
public class MsgAccessoryVISAACTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DOMConfigurator.configure( "resources/log4j.xml");
	}


	private IMsgAccessory msgAccessory;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		msgAccessory = new MsgAccessoryImpl();
	}

	/**
	 * @throws ISOException
	 * @throws UnsupportedEncodingException
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 * @link https://jira.wirecard.sys/browse/AIP-947
	 */
	@Test
	public void testAuthorizationReversalWithDE62shouldReturnValue()
			throws ISOException, IllegalStateException,
			IllegalArgumentException, UnsupportedEncodingException {
		// TestData Tran ID: 19227872 MTI 0400 Authorization Reversal mit DE 62
		String msg = "16010200D600000079542500000000000000000000000400F22464810CE08016000000400000000010400601FFFFFF0013000000000000002238012812282961059615110780080401205906428104F4F0F2F8F1F2F6F1F0F5F9F6F9F5F0F1F1F583F0F0F0F0F0F0F1F1F0F0F383F0F0F0F0F0F0F1404040C6C3C240E28899A340D5819485404040404040404040404040C6C3C240E28899A340D3968340E4C10978050900000005104000000000000000041402859374059607A0000000002501010000000000000000000000000000000000000000";

		String fieldValue = msgAccessory.getFieldValue(msg, "VISA", "62.2");
		assertEquals("SubField 62.2 was not read correctly.",
				"0414028593740596", fieldValue);

	}

	@Test
	public void testAuthorizationReversalWithDE3shouldReturnValue()
			throws ISOException, IllegalStateException,
			IllegalArgumentException, UnsupportedEncodingException {
		// TestData Tran ID: 19227872 MTI 0400 Authorization Reversal mit DE 62
		String msg = "16010200D600000079542500000000000000000000000400F22464810CE08016000000400000000010400601FFFFFF0013000000000000002238012812282961059615110780080401205906428104F4F0F2F8F1F2F6F1F0F5F9F6F9F5F0F1F1F583F0F0F0F0F0F0F1F1F0F0F383F0F0F0F0F0F0F1404040C6C3C240E28899A340D5819485404040404040404040404040C6C3C240E28899A340D3968340E4C10978050900000005104000000000000000041402859374059607A0000000002501010000000000000000000000000000000000000000";

		String fieldValue33 = msgAccessory.getFieldValue(msg, "VISA", "3.3");
		assertEquals("SubField 3.3 was not read correctly.", "00", fieldValue33);

	}

	@Test
	public void testAuthorizationResponseWithDE62_3shouldReturnValue()
			throws ISOException, IllegalStateException,
			IllegalArgumentException, UnsupportedEncodingException {
		// TestData Tran ID: 19227866 MTI 0110 Authorization Reversal mit DE 62
		String msg = "160102008C79542500000100000000000000000000000110722020810ED0800610400601FFFFFF0013000000000000002238012812280761059608045906428104F4F0F2F8F1F2F6F1F0F5F9F6F9F5F0F1F1F5F0F083F0F0F0F0F0F0F1F1F0F0F383F0F0F0F0F0F0F140404002F5E809781460000000000000000414028593740596F7C6F4F5058000000002";

		// String fieldValue33 = msgAccessory.getFieldValue(msg, "VISA", "3.3");
		// assertEquals("SubField 3.3 was not read correctly.", "00",
		// fieldValue33);

		String fieldValue622 = msgAccessory.getFieldValue(msg, "VISA", "62.2");
		assertEquals("SubField 62.2 was not read correctly.",
				"0414028593740596", fieldValue622);

		String fieldValue = msgAccessory.getFieldValue(msg, "VISA", "62.3");
		assertEquals("SubField 62.3 was not read correctly.", "7F45",
				fieldValue);

	}

	@Test
	public void testAuthorizationRequestReturnValues()
			throws ISOException, IllegalStateException,
			IllegalArgumentException, UnsupportedEncodingException {
		// TranID 18842255
		String twoInput = "16010200B300000079542500000000000000000000000100F224648108E08012000000000000000410412435FFFFFF0019000000000000033333012308265160121415111711035601200006450476F4F0F2F3F0F8F6F0F1F2F1F485F0F0F0F0F0F0F1F1F0F0F585F0F0F0F0F0F0F1404040C9D6C240E2889699A340D58194854040404040404040404040C9D6C240E2889699A340D39683C9D5097801090580000000000E0040000000000000F1F140C6C6C6";

		String fieldValue33 = msgAccessory.getFieldValue(twoInput, "VISA",
				"3.3");
		assertEquals("SubField 3.3 was not read correctly.", "00", fieldValue33);

		String fieldValue222 = msgAccessory.getFieldValue(twoInput, "VISA",
				"22.2");
		assertEquals("SubField 3.3 was not read correctly.", "12",
				fieldValue222);

		String fieldValue631 = msgAccessory.getFieldValue(twoInput, "VISA",
				"63.1");
		assertEquals("SubField 63.2 was not read correctly.", "0000",
				fieldValue631);

		String fieldValue601 = msgAccessory.getFieldValue(twoInput, "VISA", "60.1");
		assertEquals("SubField 60.1 was not read correctly.", "0", fieldValue601);
		
//		String fieldValue = msgAccessory.getFieldValue(twoInput, "VISA", "60.2");
//		assertEquals("SubField 60.1 was not read correctly.", "9", fieldValue);

		String fieldValue41 = msgAccessory
				.getFieldValue(twoInput, "VISA", "41");
		assertEquals("Field 41 was not read correctly.", "e0000001",
				fieldValue41);

		String fieldValue126_10 = msgAccessory.getFieldValue(twoInput, "VISA",
				"126.10");
		assertEquals("SubField 63.2 was not read correctly.", "11 FFF",
				fieldValue126_10);

	}

}
