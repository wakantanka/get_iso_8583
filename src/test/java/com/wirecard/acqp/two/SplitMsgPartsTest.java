package com.wirecard.acqp.two;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class SplitMsgPartsTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        DOMConfigurator.configure("resources/log4j.xml");

    }


    @Test
    public void testSeperateBitMap() throws DecoderException {
        assertEquals(
                "723C440188E18008",
                MsgAccessoryImpl.getMCBitMap("F0F1F0F0723C440188E18008F1F9F5F4F0F5F6F2F0F0F0F0F0F0F0F0F0F0F0F1F4F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F5F0F5F0F1F2F0F1F3F0F3F0F9F5F8F9F2F7F8F1F3F0F3F0F9F0F1F2F0F1F5F1F1F5F4F1F1F8F1F2F0F6F0F1F3F4F0F1F0F6F2F0F0F3F5F0F0F1F2F0F1F4F5F4F9F3F5F482F0F0F0F0F0F0F1D9C5E3D382F0F0F0F0F0F0F1404040C3C3C240E3F140E28899A340D581948540404040404040C3C3C240E3F140E28899A340D340D7C1D5F0F6F0E3F6F1F0F5F0F0F0F0F1F9F2F0F35C5C5CF4F2F0F7F0F1F0F3F2F1F2F4F3F2F891C982A884F6E38581889492C1C2C5C1C1C1C699D894A8E7A694F07EF9F7F8F0F2F1F1F0F2F5F1F0F0F0F0F6F0F0F0F5F9F1D7C1D5F1F2"));
    }


    @Test
    public void testStripFs() {
        assertEquals("01100", MsgUtils.stripFs("F0F1F1F0F0"));

    }

    @Test(expected = NumberFormatException.class)
    public void testStripFsNeg() {
        MsgUtils.stripFs("F0F181F0F0");

    }

    @Test
    public void testDecodeNibbleHex() throws DecoderException {
        byte[] bytes = StringUtils
                .getBytesUtf8("F0F1F0F0723C440188E18008F1F9F5F4F0F5F6F2F0F0F0F0F0F0F0F0F0F0F0F1F4F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F5F0F5F0F1F2F0F1F3F0F3F0F9F5F8F9F2F7F8F1F3F0F3F0F9F0F1F2F0F1F5F1F1F5F4F1F1F8F1F2F0F6F0F1F3F4F0F1F0F6F2F0F0F3F5F0F0F1F2F0F1F4F5F4F9F3F5F482F0F0F0F0F0F0F1D9C5E3D382F0F0F0F0F0F0F1404040C3C3C240E3F140E28899A340D581948540404040404040C3C3C240E3F140E28899A340D340D7C1D5F0F6F0E3F6F1F0F5F0F0F0F0F1F9F2F0F35C5C5CF4F2F0F7F0F1F0F3F2F1F2F4F3F2F891C982A884F6E38581889492C1C2C5C1C1C1C699D894A8E7A694F07EF9F7F8F0F2F1F1F0F2F5F1F0F0F0F0F6F0F0F0F5F9F1D7C1D5F1F2");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
        }
        Hex hexer = new Hex();
        byte[] bytesDec = hexer.decode(bytes);
        for (int i = 0; i < bytesDec.length; i++) {
            sb.append(String.format("%01X", bytesDec[i]));
        }
    }

    @Test
    public void testDecodeEBCDIC() throws UnsupportedEncodingException {
        
        assertEquals(new String(MsgUtils.decodeNibbleHex("82F0F0F0F0F0F0F1"),
                "Cp1047"), "b0000001");
        assertEquals(
                new String(
                        MsgUtils.decodeNibbleHex("D9C5E3D382F0F0F0F0F0F0F1"),
                        "Cp1047"), "RETLb0000001");
    }
    @Test
    public void testIsHex() throws UnsupportedEncodingException {
        
        assertTrue(MsgUtils.isHex("82F0F0F0F0F0F0F1"));
        assertFalse(MsgUtils.isHex("RETLb0000001"));
    }
    
    @Test
    public void testIsFieldPath() throws UnsupportedEncodingException {

        assertTrue(MsgUtils.isFieldPath("82.44.3"));
        assertTrue(MsgUtils.isFieldPath("82.4.33"));
        assertTrue(MsgUtils.isFieldPath("4.4.33"));
        assertTrue(MsgUtils.isFieldPath("8"));
        assertTrue(MsgUtils.isFieldPath("48.1"));
        assertTrue(MsgUtils.isFieldPath("126.10"));
        assertTrue(MsgUtils.isFieldPath("123"));
        assertFalse(MsgUtils.isFieldPath("txt"));
        assertFalse(MsgUtils.isFieldPath("t.12.t"));
        assertFalse(MsgUtils.isFieldPath("t12t"));
        
    }
}
