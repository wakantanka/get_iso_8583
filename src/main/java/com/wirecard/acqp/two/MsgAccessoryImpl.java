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
    static final int MIN_TWO_INPUT_LENGTH = 40;

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
            
            injectDataToUnpack(twoInput, cardSchemeType, isoMsg);
            
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
            logger.error("UnsupportedEncodingException error in ", e);
        }
        return null;
        
    }

    /**
     * Utility AccessMethod read the hole interchange Msg.
     * 
     * @param twoInput
     *            the HexString of an ISO8583 InterchangeMsg
     * @param cardSchemeType
     *            the CardScheme allowed Values VISA, MASTERCARD, JCB
     * @param format
     *           PlainText or xml
     * @return the the hole Msg as String in choosen Format
     * @throws ISOException
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     * @throws UnsupportedEncodingException
     * 
     */
    public static String readMsg(final String twoInput,
            final String cardSchemeType, final String format)
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

            injectDataToUnpack(twoInput, cardSchemeType, isoMsg);
            
            // logging
            if (logger.isTraceEnabled()) {
                isoMsg.dump(MsgUtils.createLoggingProxy(), "");
            }
            if (format.trim().equalsIgnoreCase("txt")) {
                
                return MsgUtils.getISOMsgPlainText(isoMsg);
                // MsgUtils.logISOMsgPlainText(isoMsg);
            }
            else if (format.trim().equalsIgnoreCase("xml")) {
                
            return MsgUtils.getISOMsgPlainText(isoMsg);
            // MsgUtils.logISOMsgPlainText(isoMsg);
            }
            else {
                throw new IllegalArgumentException("invalid format");
            }

            
        } catch (UnsupportedEncodingException e) {
            logger.error("UnsupportedEncodingException error in ", e);
        }
        return null;

    }

    private static void injectDataToUnpack(final String twoInput,
            final String cardSchemeType, ISOMsg isoMsg) throws ISOException,
            UnsupportedEncodingException {
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
            isoMsg.unpack(getBytesFromTwoDataMC(twoInput));
            break;
        case JCB:
            AsciiHexInterpreter asciiIn2 = new AsciiHexInterpreter();
            byte[] jcbdataPart = asciiIn2.uninterpret(twoInput.getBytes(),
                    0, twoInput.length() / 2);
            isoMsg.unpack(jcbdataPart);
            break;

        default:
            logger.error("IllegalStateException");
            throw new IllegalStateException(
                    "Can't determine CardScheme. You schould never see this. Sorry!");

        }
    }

    /**
     * Utility AccessMethod to read MTI (Message Type Indicator). just for the
     * requirement, MTI is FieldPath 0.
     * 
     * @param twoInput
     *            the HexString of an ISO8583 InterchangeMsg
     * @param cardSchemeType
     *            the CardScheme allowed Values VISA, MASTERCARD, JCB
     * @return the Message Type Indicator
     * @throws ISOException
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     * @throws UnsupportedEncodingException
     * 
     */
    public static String parseMTI(final String twoInput,
            final String cardSchemeType) throws ISOException,
            UnsupportedEncodingException {
        return readFieldValue(twoInput, cardSchemeType, "0");
    }

    static byte[] getBytesFromTwoDataMC(final String twoInput)
            throws UnsupportedEncodingException, ISOException {
        String mti = new String(MsgUtils.decodeNibbleHex(twoInput.substring(0,
                8)), "Cp1047");
        String bitmap = new String(getMCBitMap(twoInput));

        int dataOfsset = 24;
        if (bitmap.length() > 16) // secondary Bit Map is present
        {
            dataOfsset = 40;
        }

        String dataPartMC = new String(MsgUtils.decodeNibbleHex(twoInput
                .substring(dataOfsset, twoInput.length())), "Cp1047");

        StringBuilder sb = new StringBuilder();
        sb.append(mti);
        sb.append(bitmap);
        sb.append(dataPartMC);

        String data = sb.toString();
        return (data.getBytes());
    }

    // MsgUtils.logISOMsgPlainText(isoMsg);
    static String getMCBitMap(final String input) {
        if (input.substring(24, 40).contains("F")) {
            return input.substring(8, 24);
        } else {
            return input.substring(8, 40);
        }
    }

}
