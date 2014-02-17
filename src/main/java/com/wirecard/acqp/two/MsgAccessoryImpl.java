/**
 * 
 */
package com.wirecard.acqp.two;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Hex;
import org.jpos.iso.AsciiHexInterpreter;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Wirecard AG (c) 2014. All rights reserved.
 * 
 */
public final class MsgAccessoryImpl { // implements IMsgAccessory {
    private static Logger logger = LoggerFactory
            .getLogger(MsgAccessoryImpl.class);
    static final int MIN_TWO_INPUT_LENGTH = 150; //JCB Response < 190

    private MsgAccessoryImpl() {
        // nothing - Utility classes should not have a public or default
        // constructor.
    }

    /**
     * Utility AccessMethod for requesting a specific FieldValue.
     * 
     * @param twoInput
     *            the HexString of an ISO8583 InterchangeMsg
     * @param cardSchemeType
     *            the CardScheme allowed Values VISA, MASTERCARD, JCB
     * @param fieldPath
     *            requested FieldPath e. g. 30, could include Subfields e. g.
     *            62.2
     * @return the requested fieldvalue as String
     * @throws ISOException
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     * @throws UnsupportedEncodingException
     * 
     */
    public static String readFieldValue(final String twoInput,
            final String cardSchemeType, final String fieldPath)
            throws ISOException, UnsupportedEncodingException {
        try {
            ClassLoader classLoader = Thread.currentThread()
                    .getContextClassLoader();

            if (classLoader == null) {
                classLoader = Class.class.getClassLoader();
            }

            if (twoInput == null || twoInput.length() < MIN_TWO_INPUT_LENGTH) {
                throw new IllegalArgumentException("TwoInput to short");
            }
            GenericPackager sPackager = new GenericPackager(
                    classLoader.getResourceAsStream(CardScheme.getCardScheme(
                            cardSchemeType).getPath()));

            // logging
            logger.debug("used TWOInput : " + twoInput);

            ISOMsg isoMsg = new ISOMsg();

            isoMsg.setPackager(sPackager);

            if (logger.isTraceEnabled()) {
                org.jpos.util.Logger jPlogger = new org.jpos.util.Logger();
                jPlogger.addListener(new org.jpos.util.SimpleLogListener(
                        MsgUtils.createLoggingProxy()));
                ((org.jpos.util.LogSource) sPackager).setLogger(jPlogger,
                        "debug");

            }

            switch (CardScheme.getCardScheme(cardSchemeType)) {
            case VISA:
                int VISA_DATA_PART_OFFSET = CardScheme.VISA.getDataOffset();
                String visaDataPartHex = twoInput.substring(
                        VISA_DATA_PART_OFFSET, twoInput.length());
                // convert Hex to ASCII
                AsciiHexInterpreter asciiIn = new AsciiHexInterpreter();
                byte[] dataPart = asciiIn.uninterpret(
                        visaDataPartHex.getBytes(), 0,
                        visaDataPartHex.length() / 2);
                isoMsg.unpack(dataPart);
                break;
            case MASTERCARD:
                isoMsg.unpack(MsgUtils.getBytesFromTwoDataMC(twoInput));
                break;
            case JCB:
                // throw new NotYetImpementedException();
                AsciiHexInterpreter asciiIn2 = new AsciiHexInterpreter();
                byte[] jcbdataPart = asciiIn2.uninterpret(
                        twoInput.getBytes(), 0,
                        twoInput.length() / 2);
                isoMsg.unpack(jcbdataPart);
                
                
//                isoMsg.unpack(MsgUtils.getBytesFromTwoDataJCB(twoInput));
//                isoMsg.unpack(twoInput.getBytes());
                break;

            default:
                throw new IllegalStateException(
                        "Can't determine CardScheme. You schould never see this. Sorry!");

            }
            // logging
            if (logger.isTraceEnabled()) {
                isoMsg.dump(MsgUtils.createLoggingProxy(), "");
            }
            logger.debug(MsgUtils.getISOMsgPlainText(isoMsg));
            // MsgUtils.logISOMsgPlainText(isoMsg);

            if (isoMsg.getValue(fieldPath) instanceof byte[]) {
                return Hex.encodeHexString(
                        (byte[]) isoMsg.getComponent(fieldPath).getValue())
                        .toString();
            } else {
                return isoMsg.getString(fieldPath);
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("error in ", e);
        }
        return null;

    }

    // MsgUtils.logISOMsgPlainText(isoMsg);

}
