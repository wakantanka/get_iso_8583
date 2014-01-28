/**
 * 
 */
package de.wirecard.acqp.twi;

import org.jpos.iso.ISOException;

/**
 * @author jan.wahler
 * Copyright Wirecard AG (c) 2014. All rights reserved.
 */
public interface IMsgAccessory {
	
	
	public String getFieldValue(String msg, CardScheme scheme, String fieldNo) throws ISOException;

}
