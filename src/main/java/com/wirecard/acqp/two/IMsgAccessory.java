/**
 * 
 */
package com.wirecard.acqp.two;

import java.io.UnsupportedEncodingException;

import org.jpos.iso.ISOException;

/**
 * @author Wirecard AG (c) 2014. All rights reserved.
 */
public interface IMsgAccessory {
	// TODO trennen der beiden zugriffarten,wg threadsafty bei Cardscheme und
	// twoData
	/**
	 * 
	 * @param msg
	 *            the HexString of an ISO8583 InterchangeMsg
	 * @param scheme
	 *            the CardScheme allowed Values VISA, MASTERCARD, JCB
	 * @param fieldNo
	 *            requested FieldPath e. g. 30, could include Subfields e. g.
	 *            62.2
	 * @return the requested fieldvalue as String
	 * @throws ISOException
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 *             if cardScheme can not determinied
	 * @throws UnsupportedEncodingException
	 */
	public String getFieldValue(String msg, String cardScheme, String fieldNo)
			throws ISOException, IllegalStateException,
			IllegalArgumentException, UnsupportedEncodingException;

	/**
	 * Utility AccessMethod for requesting a specific FieldValue
	 * 
	 * @param msg
	 *            the HexString of an ISO8583 InterchangeMsg
	 * @param scheme
	 *            the CardScheme allowed Values VISA, MASTERCARD, JCB
	 * @param fieldNo
	 *            requested FieldPath e. g. 30, could include Subfields e. g.
	 *            62.2
	 * @return the requested fieldvalue as String
	 * @throws ISOException
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 *             if cardScheme can not determinied
	 * @throws UnsupportedEncodingException
	 * 
	 *             public static String readFieldValue(String msg, String
	 *             cardScheme, String fieldNo) throws ISOException,
	 *             IllegalStateException, IllegalArgumentException,
	 *             UnsupportedEncodingException;
	 */

	/**
	 * AccessMethod for requesting a specific FieldValue
	 * 
	 * @param fieldNo
	 *            requested FieldPath e. g. 30, could include Subfields e. g.
	 *            62.2
	 * @return the requested fieldvalue as String
	 * @throws ISOException
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException
	 *             if no Construktor Initialisation was mad
	 * @throws UnsupportedEncodingException
	 */
	public String getFieldValue(String fieldNo) throws ISOException,
			IllegalStateException, IllegalArgumentException,
			UnsupportedEncodingException;

}
