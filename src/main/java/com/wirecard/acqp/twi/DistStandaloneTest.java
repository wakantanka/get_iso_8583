/**
 * 
 */
package com.wirecard.acqp.twi;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

/**
 * @author jan.wahler
 * Copyright Wirecard AG (c) 2014. All rights reserved.
 */
public class DistStandaloneTest {

	/**
	 * @param args none
	 * {@code} java -cp "target/distribution/msgaccessory/*;" com.wirecard.acqp.twi.DistStandaloneTest
	 * {@code} java -cp "target/distribution/msgaccessory;target/distribution/msgaccessory/lib/*;" com.wirecard.acqp.twi.DistStandaloneTest
	 * {@code} java -cp "lib/*;" com.wirecard.acqp.twi.DistStandaloneTest
	 */
	public static void main(String[] args) {
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
