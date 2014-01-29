/**
 * 
 */
package com.wirecard.acqp.twi;

import static org.junit.Assert.*;

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
public class MsgAccessoryACTest {

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
	 * @link https://jira.wirecard.sys/browse/AIP-947
	 */
	@Test
	public void testAuthorizationReversalWithDE62shouldReturnValue() throws ISOException {
		// TestData Tran ID: 19227872 MTI 0400 Authorization Reversal mit DE 62
		String msg = "16010200D600000079542500000000000000000000000400F22464810CE08016000000400000000010400601FFFFFF0013000000000000002238012812282961059615110780080401205906428104F4F0F2F8F1F2F6F1F0F5F9F6F9F5F0F1F1F583F0F0F0F0F0F0F1F1F0F0F383F0F0F0F0F0F0F1404040C6C3C240E28899A340D5819485404040404040404040404040C6C3C240E28899A340D3968340E4C10978050900000005104000000000000000041402859374059607A0000000002501010000000000000000000000000000000000000000";
		
		String fieldValue = msgAccessory.getFieldValue(msg, CardScheme.VISA, "62.2");
		assertEquals("SubField 62.2 was not read correctly.", "0414028593740596", fieldValue);
		
		
	}
	
	@Test
	public void testAuthorizationReversalWithDE3shouldReturnValue() throws ISOException {
		// TestData Tran ID: 19227872 MTI 0400 Authorization Reversal mit DE 62
		String msg = "16010200D600000079542500000000000000000000000400F22464810CE08016000000400000000010400601FFFFFF0013000000000000002238012812282961059615110780080401205906428104F4F0F2F8F1F2F6F1F0F5F9F6F9F5F0F1F1F583F0F0F0F0F0F0F1F1F0F0F383F0F0F0F0F0F0F1404040C6C3C240E28899A340D5819485404040404040404040404040C6C3C240E28899A340D3968340E4C10978050900000005104000000000000000041402859374059607A0000000002501010000000000000000000000000000000000000000";
		
		String fieldValue = msgAccessory.getFieldValue(msg, CardScheme.VISA, "3");
		assertEquals("SubField 62.2 was not read correctly.", "000000", fieldValue);
		
		
	}
	
	@Test
	public void testAuthorizationResponseWithDE62_3shouldReturnValue() throws ISOException {
		// TestData Tran ID: 19227866 MTI 0110 Authorization Reversal mit DE 62
		String msg = "160102008C79542500000100000000000000000000000110722020810ED0800610400601FFFFFF0013000000000000002238012812280761059608045906428104F4F0F2F8F1F2F6F1F0F5F9F6F9F5F0F1F1F5F0F083F0F0F0F0F0F0F1F1F0F0F383F0F0F0F0F0F0F140404002F5E809781460000000000000000414028593740596F7C6F4F5058000000002";
		
		String fieldValue3 = msgAccessory.getFieldValue(msg, CardScheme.VISA, "3");
		assertEquals("SubField 3 was not read correctly.", "000000", fieldValue3);
		
		String fieldValue622 = msgAccessory.getFieldValue(msg, CardScheme.VISA, "62.2");
		assertEquals("SubField 62.2 was not read correctly.", "0414028593740596", fieldValue622);
		
		String fieldValue = msgAccessory.getFieldValue(msg, CardScheme.VISA, "62.3");
	     assertEquals("SubField 62.3 was not read correctly.", "7F45", fieldValue);
		
		
	}

}