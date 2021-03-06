package com.wirecard.acqp.two;

/**
 * @author Wirecard AG (c) 2014. All rights reserved.
 *  http://en.wikipedia.org/wiki/Card_schemes
 */
@SuppressWarnings("javadoc")
public enum CardScheme {
    VISA("VISA", 44), MASTERCARD("MasterCard", 0), JCB("JCB", 0);

    private final String path;
    private final int dataOffset;

    /**
     * @return dataOffset
     */
    public int getDataOffset() {
        return dataOffset;
    }

    CardScheme(final String cpath, final int coffset) {
        this.path = cpath;
        this.dataOffset = coffset;
    }

    /**
     * @return path to xml field descriptions for GenericPackager
     */
    public String getPath() {
        return path + ".xml";
        // return "src/main/resources/" + path + ".xml";
    }

    /**
     * HelperMethod to use JRE 1.6 compatible string switch.
     * 
     * @param cardSchemeType
     * @return CardScheme Enum
     * @throws IllegalArgumentException
     */
    public static CardScheme getCardScheme(final String cardSchemeType) {

        if (cardSchemeType.trim().equalsIgnoreCase("VISA")) {
            return CardScheme.VISA;
        } else if (cardSchemeType.trim().equalsIgnoreCase("MASTERCARD")) {
            return CardScheme.MASTERCARD;
        } else if (cardSchemeType.trim().equalsIgnoreCase("JCB")) {
            return CardScheme.JCB;
        } else {
            throw new IllegalArgumentException("Can't determine CardScheme.");
        }
    }

}
