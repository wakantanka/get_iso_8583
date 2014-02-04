package com.wirecard.acqp.two;

/**
 * @author jan.wahler Copyright Wirecard AG (c) 2014. All rights reserved.
 * @link http://en.wikipedia.org/wiki/Card_schemes
 */
public enum CardScheme {
	VISA("base1", 44), MASTERCARD("basic", 0), JCB("basicJCB", 0);

	private String path;
	int dataOffset;

	CardScheme(String path, int offset) {
		this.path = path;
		this.dataOffset = offset;
	}

	/**
	 * @return path to xml field descriptions for GenericPackager
	 */
	public String getPath() {
		return "src/main/resources/" + path + ".xml";
	}

	/**
	 * HelperMethod to use JRE 1.6 compatible string switch
	 * 
	 * @param cardSchemeType
	 * @return CardScheme Enum
	 */
	public static CardScheme getCardScheme(String cardSchemeType) throws IllegalArgumentException {

		if (cardSchemeType.equalsIgnoreCase("VISA")) {
			return CardScheme.VISA;
		} else if (cardSchemeType.equalsIgnoreCase("MASTERCARD")) {
			return CardScheme.MASTERCARD;
		} else if (cardSchemeType.equalsIgnoreCase("JCB")) {
			return CardScheme.JCB;
		} else {
			throw new IllegalArgumentException();
		}
	}

}
