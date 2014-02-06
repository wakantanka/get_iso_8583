/**
 * 
 */
package com.wirecard.acqp.two;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.UnsupportedEncodingException;

import org.apache.log4j.xml.DOMConfigurator;
import org.jpos.iso.ISOException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author jan.wahler
 * 
 * 
 */
public class MsgAccessoryMasterCardACTest {
	private IMsgAccessory msgAccessory;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DOMConfigurator.configure( "resources/log4j.xml");
	}


	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		msgAccessory = new MsgAccessoryImpl();
	}

	/**
	 * @throws IllegalArgumentException 
	 * @throws IllegalStateException 
	 */
	@Test
	public void testAuthorizationDE2shouldReturnPAN() throws ISOException, IllegalStateException, IllegalArgumentException, UnsupportedEncodingException {
		// TestData Tran ID: 18749354 MTI 0100 Authorization MasterCard
		String msg = "F0F1F0F0723C440188E18008F1F9F5F4F0F5F6F2F0F0F0F0F0F0F0F0F0F0F0F1F4F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F5F0F5F0F1F2F0F1F3F0F3F0F9F5F8F9F2F7F8F1F3F0F3F0F9F0F1F2F0F1F5F1F1F5F4F1F1F8F1F2F0F6F0F1F3F4F0F1F0F6F2F0F0F3F5F0F0F1F2F0F1F4F5F4F9F3F5F482F0F0F0F0F0F0F1D9C5E3D382F0F0F0F0F0F0F1404040C3C3C240E3F140E28899A340D581948540404040404040C3C3C240E3F140E28899A340D340D7C1D5F0F6F0E3F6F1F0F5F0F0F0F0F1F9F2F0F35C5C5CF4F2F0F7F0F1F0F3F2F1F2F4F3F2F891C982A884F6E38581889492C1C2C5C1C1C1C699D894A8E7A694F07EF9F7F8F0F2F1F1F0F2F5F1F0F0F0F0F6F0F0F0F5F9F1D7C1D5F1F2";
		 
		String fieldValue = msgAccessory.getFieldValue(msg, "MASTERCARD", "2");
		assertEquals("PAN (Field 2) was not read correctly.",
				"5405620000000000014", fieldValue);
		
	}
	
	@Test
	public void testAuthorizationResponseDE2shouldReturnPAN() throws ISOException, IllegalStateException, IllegalArgumentException, UnsupportedEncodingException {
		// 19484457 , 110
		String msg = "F0F1F1F0766300018E81A002F1F6F5F4F0F0F0F4F1F2F3F4F5F6F7F8F9F8F0F0F0F0F0F0F0F0F0F0F0F0F0F1F0F0F0F0F0F0F0F0F0F0F0F1F2F0F1F5F0F2F0F4F1F4F4F7F3F1F6F1F2F0F1F4F9F0F6F2F5F0F0F0F0F2F0F4F0F2F0F4F0F6F0F1F3F4F0F1F0F6F2F0F0F3F5F0F0F2F0F4F0F9F2F8F4F4F5F7F0F0F5F8F9F6F0F081F0F0F0F1F4F7F0F0F2F4E3F4F2F0F7F0F1F0F3F2F1F0F8F7F0F1E4F9F2F0F35C5C5CF9F7F8F8F4F0F0F0F9D4C3C3F0F1F1F8F7F3";
		
		String fieldValue = msgAccessory.getFieldValue(msg, "MASTERCARD", "2");
		assertEquals("PAN (Field 2) was not read correctly.",
				"5400041234567898", fieldValue);
		
	}
	
	@Test
	public void testAuthorizationSubfields() throws ISOException, IllegalStateException, IllegalArgumentException, UnsupportedEncodingException {
		// TestData Tran ID: 18749354 MTI 0100 Authorization MasterCard
		String msg = "F0F1F0F0723C440188E18008F1F9F5F4F0F5F6F2F0F0F0F0F0F0F0F0F0F0F0F1F4F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F5F0F5F0F1F2F0F1F3F0F3F0F9F5F8F9F2F7F8F1F3F0F3F0F9F0F1F2F0F1F5F1F1F5F4F1F1F8F1F2F0F6F0F1F3F4F0F1F0F6F2F0F0F3F5F0F0F1F2F0F1F4F5F4F9F3F5F482F0F0F0F0F0F0F1D9C5E3D382F0F0F0F0F0F0F1404040C3C3C240E3F140E28899A340D581948540404040404040C3C3C240E3F140E28899A340D340D7C1D5F0F6F0E3F6F1F0F5F0F0F0F0F1F9F2F0F35C5C5CF4F2F0F7F0F1F0F3F2F1F2F4F3F2F891C982A884F6E38581889492C1C2C5C1C1C1C699D894A8E7A694F07EF9F7F8F0F2F1F1F0F2F5F1F0F0F0F0F6F0F0F0F5F9F1D7C1D5F1F2";
		
		String fieldValue48_43 = msgAccessory.getFieldValue(msg, "MASTERCARD", "48.43");
		assertEquals("SubField 48.43 was not read correctly.",
				"jIbyd6TeahmkABEAAAFrQmyXwm0=", fieldValue48_43);
		
		String fieldValue61_4 = msgAccessory.getFieldValue(msg, "MASTERCARD", "61.4");
		assertEquals("SubField 48.43 was not read correctly.",
				"5", fieldValue61_4);
	}
	
//	@Test
	public void testAuthorizationSubSubfields() throws ISOException, IllegalStateException, IllegalArgumentException, UnsupportedEncodingException {
		// TestData Tran ID: 18749354 MTI 0100 Authorization MasterCard
		String msg = "F0F1F0F0723C440188E18008F1F9F5F4F0F5F6F2F0F0F0F0F0F0F0F0F0F0F0F1F4F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F5F0F5F0F1F2F0F1F3F0F3F0F9F5F8F9F2F7F8F1F3F0F3F0F9F0F1F2F0F1F5F1F1F5F4F1F1F8F1F2F0F6F0F1F3F4F0F1F0F6F2F0F0F3F5F0F0F1F2F0F1F4F5F4F9F3F5F482F0F0F0F0F0F0F1D9C5E3D382F0F0F0F0F0F0F1404040C3C3C240E3F140E28899A340D581948540404040404040C3C3C240E3F140E28899A340D340D7C1D5F0F6F0E3F6F1F0F5F0F0F0F0F1F9F2F0F35C5C5CF4F2F0F7F0F1F0F3F2F1F2F4F3F2F891C982A884F6E38581889492C1C2C5C1C1C1C699D894A8E7A694F07EF9F7F8F0F2F1F1F0F2F5F1F0F0F0F0F6F0F0F0F5F9F1D7C1D5F1F2";

		String fieldValue48_61_1 = msgAccessory.getFieldValue(msg, "MASTERCARD", "48.2.1");
		assertEquals("SubField 48.61.1 was not read correctly.",
				"0", fieldValue48_61_1);
		fail("access should be over 48.61.1"); 
		
//		String fieldValue48_61_5 = msgAccessory.getFieldValue(msg, "MASTERCARD", "48.2.5");
//		assertEquals("SubField 48.61.5 was not read correctly.",
//				"1", fieldValue48_61_5);
		
		
	}

	@Test (expected = IllegalArgumentException.class)
	public void testGetFieldValueWrongInputParamater() throws IllegalArgumentException, ISOException, IllegalStateException, UnsupportedEncodingException {
		
		@SuppressWarnings("unused")
		String fieldValue = msgAccessory.getFieldValue("nil",
				"MeiSTERkARD", "2");
	}
	
	@Test (expected = IllegalStateException.class)
	public void testGetFieldValueWithoutTwoData() throws IllegalArgumentException, ISOException, IllegalStateException, UnsupportedEncodingException {

		@SuppressWarnings("unused")
		String fieldValue = msgAccessory.getFieldValue("2");
	}
	
	@Test(expected = IllegalStateException.class)
	public void testGetFieldValueWithoutTwoInputParamater()
			throws IllegalArgumentException, ISOException,
			IllegalStateException, UnsupportedEncodingException {
		@SuppressWarnings("unused")
		String fieldValue = msgAccessory.getFieldValue(null, "VISA", "2");
	}
	@Test(expected = IllegalArgumentException.class)
	public void testGetFieldValueToShortTwoInputParamater()
			throws IllegalArgumentException, ISOException,
			IllegalStateException, UnsupportedEncodingException {
		@SuppressWarnings("unused")
		String fieldValue = msgAccessory.getFieldValue("0F1F0F0723C440188E18008F1F9F5F4F0F5F6F2F0F0F0F0F0F0F0F0F0F0F0F1F4F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F5F0F5F0F1F2F0F1F3F0F3F0F9F5F8F9F2F7F8F1F3F0F3F0F9F0F1F", "VISA", "2");
	}
}
