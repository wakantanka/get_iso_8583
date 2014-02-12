package com.wirecard.acqp.two;

/**
 * @author Wirecard AG (c) 2014. All rights reserved.
 * @link http://en.wikipedia.org/wiki/Card_schemes
 */
public enum CardScheme {
	VISA("base1", 44), MASTERCARD("basic", 0), JCB("basicJCB", 0);

	private final String path;
	private final int dataOffset;

	CardScheme(final String cpath, final int coffset) {
		this.path = cpath;
		this.dataOffset = coffset;
	}

	/**
	 * @return path to xml field descriptions for GenericPackager
	 */
	public String getPath() {
		return "src/main/resources/" + path + ".xml";
	}

	/**
	 * HelperMethod to use JRE 1.6 compatible string switch.
	 * 
	 * @param cardSchemeType
	 * @return CardScheme Enum
	 * @throws IllegalArgumentException
	 */
	public static CardScheme getCardScheme(final String cardSchemeType) {

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
