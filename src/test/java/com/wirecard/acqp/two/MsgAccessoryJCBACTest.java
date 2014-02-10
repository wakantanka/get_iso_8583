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
public class MsgAccessoryJCBACTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
//		DOMConfigurator.configure( "resources/log4j.xml");
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
	 * @throws IllegalArgumentException 
	 * @throws IllegalStateException 
	 */
//	@Test 
	@Test (expected = NotYetImpementedException.class)
	public void testAuthorizationDE2shouldReturnPAN() throws ISOException, IllegalStateException, IllegalArgumentException, UnsupportedEncodingException {
		// TestData Tran ID: 19274374 MTI 0100 Authorization jcb-request
		String msg = "F0F1F0F0723C448188C1800810352800FFFFFF7894000000000000400030012912071168530912071101291612074281200108887690000888769000F0F0F0F0F1F9F2F7F4F3F7F481F0F0F0F1F4F7F0F1F0F0F181F0F0F0F1F4F7F040404033F0F2F4F76DC855BD1DC6415D8FA6EA3D217D218800000104000002051089609222132828618960000000000005404040404040F9F7F806F2F2F2F2F7F6";
		
		String fieldValue = msgAccessory.getFieldValue(msg, "JCB", "2");
		assertEquals("PAN (Field 2) was not read correctly.", "352800FFFFFF7894", fieldValue);
		
		
	}
	
//	@Test
	public void testAuthorizationReversalWithDE3shouldReturnValue() throws ISOException, IllegalStateException, IllegalArgumentException, UnsupportedEncodingException {
		// TestData Tran ID: 19227872 MTI 0400 Authorization Reversal mit DE 62
		String msg = "F0F1F0F0723C448188C1800810352800FFFFFF7894000000000000400030012912071168530912071101291612074281200108887690000888769000F0F0F0F0F1F9F2F7F4F3F7F481F0F0F0F1F4F7F0F1F0F0F181F0F0F0F1F4F7F040404033F0F2F4F76DC855BD1DC6415D8FA6EA3D217D218800000104000002051089609222132828618960000000000005404040404040F9F7F806F2F2F2F2F7F6 ";
		
		String fieldValue = msgAccessory.getFieldValue(msg, "JCB", "3");
		assertEquals("SubField 62.2 was not read correctly.", "000000", fieldValue);
		
		
	} 
}
