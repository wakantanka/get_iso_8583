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
	 * @param args
	 */
	public static void main(String[] args) {
		ISOMsg isoMsg = new ISOMsg();
		try {
			isoMsg.setMTI("0100");
		} catch (ISOException e) {
			e.printStackTrace();
		}

	}

}
