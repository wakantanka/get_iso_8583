package com.wirecard.acqp.two;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

/**
 * @author Wirecard AG (c) 2014. All rights reserved.
 */
public final class DistStandaloneTest {
    private DistStandaloneTest() {
        // nothing - Utility classes should not have a public or default
        // constructor.
    }

    /**
     * @param args
     *            none {@code} java -cp
     *            "target/distribution/interchange-msg-accessory-distribution/interchange-msg-accessory/*;"
     *            com.wirecard.acqp.two.DistStandaloneTest
     */
    public static void main(final String[] args) {
        try {
            ISOMsg isoMsg = new ISOMsg();
            isoMsg.setMTI("0100");
            System.out.println("DistStandaloneTest ok");

        } catch (ISOException e) {
            System.err.println("DistStandaloneTest failed");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("DistStandaloneTest failed");
            e.printStackTrace();
        }

    }

}
