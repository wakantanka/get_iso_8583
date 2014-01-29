/**
 * 
 */
package com.wirecard.acqp.twi;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;

import org.jpos.iso.ISOException;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.wirecard.acqp.twi.CardScheme;
import com.wirecard.acqp.twi.IMsgAccessory;
import com.wirecard.acqp.twi.MsgAccessoryImpl;

/**
 * @author jan.wahler
 * 
 * 
 */
public class MsgAccessoryMasterCardACTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
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
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * @throws ISOException
	 * @throws UnsupportedEncodingException 
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

		// RandomAccess
		// assertEquals("SubField 48.43 not read correct",
		// "jIbyd6TeahmkABEAAAFrQmyXwm0=", isoMsg.getString("48.43"));
		//
		// assertEquals("could not read Field 61", "1025100006000591PAN12",
		// isoMsg
		// .getComponent(61).getValue().toString());

	}

	@Test (expected = IllegalArgumentException.class)
	public void testGetFieldValueWrongInputParamater() throws IllegalArgumentException, ISOException, IllegalStateException, UnsupportedEncodingException {

		String fieldValue = msgAccessory.getFieldValue("nil",
				"MeiSTERkARD", "2");
	}
}
