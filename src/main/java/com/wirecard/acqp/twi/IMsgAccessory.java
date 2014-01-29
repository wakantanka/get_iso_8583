/**
 * 
 */
package com.wirecard.acqp.twi;

import org.jpos.iso.ISOException;

/**
 * @author jan.wahler
 * Copyright Wirecard AG (c) 2014. All rights reserved.
 */
public interface IMsgAccessory {
	
	/**
	 * AccessMethod for requesting a specific FieldValue
	 * 
	 * @param msg		the HexString of an ISO8583 InterchangeMsg
	 * @param scheme	the CardScheme allowed Values VISA, MASTERCARD, JCB
	 * @param fieldNo	requested FieldPath e. g. 30, could include Subfields e. g. 62.2	
	 * @return 			the requested fieldvalue as String
	 * @throws			ISOException, IllegalArgumentException
	 * {@link ISOException}
	 * {@link IllegalArgumentException} 
	 */
	public String getFieldValue(String msg, CardScheme scheme, String fieldNo) throws ISOException;

}
