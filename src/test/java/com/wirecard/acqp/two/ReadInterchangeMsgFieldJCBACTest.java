/**
 * 
 */
package com.wirecard.acqp.two;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.UnsupportedEncodingException;

import org.apache.log4j.xml.DOMConfigurator;
import org.jpos.iso.ISOException;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Wirecard AG (c) 2014. All rights reserved.
 */
@SuppressWarnings("javadoc")
public class ReadInterchangeMsgFieldJCBACTest {
    private static final String twoJCBMsg = "F0F1F0F0723C448188C1800810352800FFFFFF7894000000000000400030012912071168530912071101291612074281200108887690000888769000F0F0F0F0F1F9F2F7F4F3F7F481F0F0F0F1F4F7F0F1F0F0F181F0F0F0F1F4F7F040404033F0F2F4F76DC855BD1DC6415D8FA6EA3D217D218800000104000002051089609222132828618960000000000005404040404040F9F7F806F2F2F2F2F7F6";

    /** 
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
//        DOMConfigurator.configure("src/test/resources/log4j_trace.xml");
         DOMConfigurator.configure("resources/log4j.xml");
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
    @Test
    public void testAuthorizationDE2shouldReturnPAN() throws ISOException,
            IllegalStateException, IllegalArgumentException,
            UnsupportedEncodingException {
        // TestData Tran ID: 19274374 MTI 0100 Authorization jcb-request

        String fieldValue = MsgAccessoryImpl.readFieldValue(twoJCBMsg, "JCB",
                "2");
        assertEquals("PAN (Field 2) was not read correctly.",
                "352800FFFFFF7894", fieldValue);

    }

    @Test
    public void testAuthorizationDE41TerminalshouldReturnValue()
            throws ISOException, IllegalStateException,
            IllegalArgumentException, UnsupportedEncodingException {

        String fieldValue = MsgAccessoryImpl.readFieldValue(twoJCBMsg, "JCB",
                "41");
        assertEquals("Field 41 was not read correctly.", "a0001470", fieldValue);
    }

    @Test
    public void testAuthorizationDE49CurrencyCodeShouldReturnValue()
            throws ISOException, IllegalStateException,
            IllegalArgumentException, UnsupportedEncodingException {

        String fieldValue = MsgAccessoryImpl.readFieldValue(twoJCBMsg, "JCB",
                "49");
        assertEquals("Field 49 was not read correctly.", "978", fieldValue);
    }

    @Test
    public void testAuthorizationDE61TerminalshouldReturnValue()
            throws ISOException, IllegalStateException,
            IllegalArgumentException, UnsupportedEncodingException {
        
        String fieldValue = MsgAccessoryImpl.readFieldValue(twoJCBMsg, "JCB",
                "61");
        System.out.println("/" + fieldValue + "/");
        assertEquals("Field 61 was not read correctly.", "222276",
                fieldValue.substring(1)); // @TODO Why?
    }
    
    @Test
    public void testAuthorizationMTIshouldReturnValue()
            throws ISOException, IllegalStateException,
            IllegalArgumentException, UnsupportedEncodingException {

        String fieldValue = MsgAccessoryImpl.readFieldValue(twoJCBMsg, "JCB",
                "0");
        System.out.println("/" + fieldValue + "/");
        assertEquals("MTI was not read correctly.", "0100",
                fieldValue);  
    }

    @Test
    public void testAuthorizationDE22shouldReturnValue() throws ISOException,
            IllegalStateException, IllegalArgumentException,
            UnsupportedEncodingException {

        String fieldValue = MsgAccessoryImpl.readFieldValue(twoJCBMsg, "JCB",
                "22.2");
        assertEquals("SubField 22.2 was not read correctly.", "12", fieldValue);

    }
    @Test
    public void testDE48shouldReturnValue() throws ISOException,
    IllegalStateException, IllegalArgumentException,
    UnsupportedEncodingException {
        
        String fieldValue = MsgAccessoryImpl.readFieldValue(twoJCBMsg, "JCB",
                "48");
        assertNotNull(
                "Field 48 was not read correctly.",  fieldValue);
        
    }
    @Ignore
    @Test
    public void test3D_SecureDE48_2_3shouldReturnValue() throws ISOException,
            IllegalStateException, IllegalArgumentException,
            UnsupportedEncodingException {

        String fieldValue = MsgAccessoryImpl.readFieldValue(twoJCBMsg, "JCB",
                "48.2.3");
        assertEquals(
                "SubField 48.2.3 3D-Secure ECI (Electronic Commerce Indicator) was not read correctly.",
                "05", fieldValue);

    }

    @Ignore
    @Test
    public void testNon3D_SecureDE48_2_3shouldReturnValue()
            throws ISOException, IllegalStateException,
            IllegalArgumentException, UnsupportedEncodingException {
        String parserTwoJCBMsg = "F0F1F0F0723C448188C1800810352800FFFFFF7894000000000000010000020417240209509817240002041510074281200008887690000888769000F0F0F0F0F1F9F4F8F6F8F5F281F0F0F0F1F4F7F0F1F0F0F181F0F0F0F1F4F7F040404007F0F1F0F35C5C5CF9F7F806F2F2F2F2F7F6";

        String fieldValue = MsgAccessoryImpl.readFieldValue(parserTwoJCBMsg,
                "JCB", "48.2.3");
        assertEquals(
                "SubField 48.2.3 3D-Secure ECI (Electronic Commerce Indicator) was not read correctly.",
                "05", fieldValue);

    }
}
