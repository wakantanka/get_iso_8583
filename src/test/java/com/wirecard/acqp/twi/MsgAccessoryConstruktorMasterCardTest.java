/**
 * 
 */
package com.wirecard.acqp.twi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.UnsupportedEncodingException;

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
public class MsgAccessoryConstruktorMasterCardTest {

	private static IMsgAccessory msgAccessoryInitial;
	private static IMsgAccessory msgAccessoryRecurrring;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		String msgInitial = "F0F1F0F0723C440188E18008F1F6F5F4F0F0F0F4F1F2F3F4F5F6F7F8F9F8F0F0F0F0F0F0F0F0F0F0F0F0F2F3F0F0F0F0F0F1F2F9F1F4F3F3F2F2F6F1F2F7F0F3F1F4F3F3F2F2F0F1F2F9F1F5F1F0F0F7F4F2F8F1F2F0F6F0F1F3F4F0F1F0F6F2F0F0F3F5F0F0F1F2F9F0F9F2F8F0F4F6F581F0F0F0F1F4F7F0F1F0F0F181F0F0F0F1F4F7F0404040D3A494A494828140404040404040404040404040404040D28995A28881A281404040404040C4C5E4F0F2F8E3F6F1F0F5F0F0F0F0F1F9F2F0F35C5C5CF4F2F0F7F0F1F0F3F2F1F0F9F7F8F0F2F1F1F0F2F4F1F0F0F0F0F6F0F0F0F2F8F0F8F5F6F0F9";
		String msgRecurr  = "F0F1F0F0723C440188E18008F1F6F5F4F0F0F0F4F1F2F3F4F5F6F7F8F9F8F0F0F0F0F0F0F0F0F0F0F0F0F2F3F0F0F0F0F0F1F2F9F1F4F3F3F3F8F6F1F2F7F0F9F1F4F3F3F3F7F0F1F2F9F1F5F1F0F0F7F4F2F8F1F2F0F6F0F1F3F4F0F1F0F6F2F0F0F3F5F0F0F1F2F9F0F9F2F8F0F4F7F481F0F0F0F1F4F7F0F1F0F0F181F0F0F0F1F4F7F0404040D3A494A494828140404040404040404040404040404040D28995A28881A281404040404040C4C5E4F0F2F8E3F6F1F0F5F0F0F0F0F1F9F2F0F35C5C5CF4F2F0F7F0F1F0F3F2F1F0F9F7F8F0F2F1F1F0F2F4F1F0F0F0F0F6F0F0F0F2F8F0F8F5F6F0F9";

		// TestData Tran ID: 19280465 MTI 0100 MC Initial Authorization Request
		// PAN 5400041234567898
		// 61 102410000600028085609
		// 42 "1001a0001470   "
		// 43 1='Lumumba '; 2=' '; 3='Kinshasa '; 4=' '; 5='DEU'
		// 48 1='T'; 42='0103210'; 61='00001'; 92='***'

		// TestData Tran ID: 19280474 MTI 0100 und MC Repeated (Recurring??)
		// Authorization Request
		// 19280474
		//
		// PAN 5400041234567898
		// 61 102410000600028085609

		assertNotEquals("Messages identical.", msgInitial, msgRecurr);

		msgAccessoryInitial = new MsgAccessoryImpl(msgInitial, "MASTERCARD");
		msgAccessoryRecurrring = new MsgAccessoryImpl(msgRecurr, "MASTERCARD");
	}

	/**
	 * @throws ISOException
	 * @throws UnsupportedEncodingException
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 */
	@Test
	public void testAuthorizationRecurringEquality() throws ISOException,
			IllegalStateException, IllegalArgumentException,
			UnsupportedEncodingException {

		assertEquals("PAN (Field 2) was not equal.",
				msgAccessoryInitial.getFieldValue("2"), msgAccessoryRecurrring.getFieldValue("2"));
		
		assertEquals("POS Cardholder Presence was not equal.",
				msgAccessoryInitial.getFieldValue("61.4"), msgAccessoryRecurrring.getFieldValue("61.4"));
		
		assertEquals("Merchant City \"43.3\" was not equal.",
				msgAccessoryInitial.getFieldValue("43.3"), msgAccessoryRecurrring.getFieldValue("43.3"));

		// TC API-2001, but field not filled
		assertEquals("TraceID was not equal.",
				msgAccessoryInitial.getFieldValue("48.63"), msgAccessoryRecurrring.getFieldValue("48.63"));
	
		assertEquals("42 was not correct.",
				"1001a0001470   ",  msgAccessoryRecurrring.getFieldValue("42"));


	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetFieldValueWrongCardSchemeParamater()
			throws IllegalArgumentException, ISOException,
			IllegalStateException, UnsupportedEncodingException {
		@SuppressWarnings("unused")
		IMsgAccessory msgAccessory = new MsgAccessoryImpl("nil", "MeiSTERkARD");
	}

}
