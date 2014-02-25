package com.wirecard.acqp.two;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
@SuppressWarnings("javadoc")
public class ThreadingLongrunningTest {
    /**
     * The number of threads to use in this test.
     */
    private static final int NUM_THREADS = 12;

    /**
     * Flag to indicate the concurrent test has failed.
     */
    private boolean failed;

    @Test
    public void testConcurrently() {
        final ParserHardeningLongrunningTest pt = new ParserHardeningLongrunningTest();
        final  String msg = "F0F1F0F0723C440188E18008F1F9F5F4F0F5F6F2F0F0F0F0F0F0F0F0F0F0F0F1F4F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F5F0F5F0F1F2F0F1F3F0F3F0F9F5F8F9F2F7F8F1F3F0F3F0F9F0F1F2F0F1F5F1F1F5F4F1F1F8F1F2F0F6F0F1F3F4F0F1F0F6F2F0F0F3F5F0F0F1F2F0F1F4F5F4F9F3F5F482F0F0F0F0F0F0F1D9C5E3D382F0F0F0F0F0F0F1404040C3C3C240E3F140E28899A340D581948540404040404040C3C3C240E3F140E28899A340D340D7C1D5F0F6F0E3F6F1F0F5F0F0F0F0F1F9F2F0F35C5C5CF4F2F0F7F0F1F0F3F2F1F2F4F3F2F891C982A884F6E38581889492C1C2C5C1C1C1C699D894A8E7A694F07EF9F7F8F0F2F1F1F0F2F5F1F0F0F0F0F6F0F0F0F5F9F1D7C1D5F1F2";
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    pt.testParserHardeningJCB();
                    pt.testParserHardeningMC();

                    String fieldValue = MsgAccessoryImpl.readFieldValue(msg, "MASTERCARD",
                            "2");
                    assertEquals("PAN (Field 2) was not read correctly.",
                            "5405620000000000014", fieldValue);
                    
                    String mti = MsgAccessoryImpl.readFieldValue(msg, "MASTERCARD",
                            "0");
                    assertEquals("mti was not read correctly.",
                            "0100", mti);
                    
                    pt.testParserHardeningVisa();
                } catch (Exception e) {
                    failed = true;
                }
            }
        };

        Thread[] threads = new Thread[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i] = new Thread(runnable);
        }
        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i].start();
        }
        for (int i = 0; i < NUM_THREADS; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                failed = true;
            }
        }

        assertFalse(failed);
        
    }
    // @Test obsolet but temp
    // public void testGetFieldValueMixedgetFieldMethods()
    // throws IllegalArgumentException, ISOException,
    // IllegalStateException, UnsupportedEncodingException {
    //
    // // UtilityMethodAccess after construktor
    // String msg =
    // "F0F1F0F0723C440188E18008F1F9F5F4F0F5F6F2F0F0F0F0F0F0F0F0F0F0F0F1F4F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F5F0F5F0F1F2F0F1F3F0F3F0F9F5F8F9F2F7F8F1F3F0F3F0F9F0F1F2F0F1F5F1F1F5F4F1F1F8F1F2F0F6F0F1F3F4F0F1F0F6F2F0F0F3F5F0F0F1F2F0F1F4F5F4F9F3F5F482F0F0F0F0F0F0F1D9C5E3D382F0F0F0F0F0F0F1404040C3C3C240E3F140E28899A340D581948540404040404040C3C3C240E3F140E28899A340D340D7C1D5F0F6F0E3F6F1F0F5F0F0F0F0F1F9F2F0F35C5C5CF4F2F0F7F0F1F0F3F2F1F2F4F3F2F891C982A884F6E38581889492C1C2C5C1C1C1C699D894A8E7A694F07EF9F7F8F0F2F1F1F0F2F5F1F0F0F0F0F6F0F0F0F5F9F1D7C1D5F1F2";
    // assertEquals("PAN (Field 2) was not read correctly.",
    // "5405620000000000014",
    // msgAccessoryInitial.getFieldValue(msg, "MASTERCARD", "2"));
    //
    // assertEquals("PAN (Field 2) was not equal.",
    // "5400041234567898",
    // msgAccessoryInitial.getFieldValue("2"));
    //
    // // UtilityMethodAccess after construktor
    // assertEquals("PAN (Field 2) was not read correctly.",
    // "5405620000000000014",
    // msgAccessoryInitial.getFieldValue(msg, "MASTERCARD", "2"));
    //
    // }

}
