package com.wirecard.acqp.two;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.codec.binary.Hex;
import org.jpos.iso.AsciiHexInterpreter;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author Wirecard AG (c) 2014. All rights reserved.
 */
public final class MsgUtils {

    private MsgUtils() {
        // nothing - Utility classes should not have a public or default
        // constructor.
    }

    private static Logger logger = LoggerFactory.getLogger(MsgUtils.class);

    static byte[] decodeNibbleHex(final String input) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        char[] chars = input.toCharArray();
        for (int i = 0; i < chars.length - 1; i += 2) {
            char[] bChars = new char[2];
            bChars[0] = chars[i];
            bChars[1] = chars[(i + 1)];
            int val = Integer.decode("0x" + new String(bChars)).intValue();
            baos.write((byte) val);
        }
        return baos.toByteArray();
    }

    static String stripFs(final String input) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {

            if (i % 2 == 0 && input.charAt(i) != 'F') {
                throw new NumberFormatException("Wrong Format for F-Nibbles");
            }
            if (i % 2 == 1) {
                {
                    result.append(input.charAt(i));
                }
            }
        }
        return result.toString();
    }

    static String stripAllFs(final String input) {
        return stripAllFs(input, 0);
    }

    static String stripAllFs(final String input, final int offset) {
        StringBuilder result = new StringBuilder();
        result.append(input.substring(0, offset));
        for (int i = offset; i < input.length(); i++) {

            if (input.charAt(i) != 'F') {
                result.append(input.charAt(i));
            }
        }
        return result.toString();
    }

    static String getISOMsgPlainText(final ISOMsg msg) {
        StringBuilder sb = new StringBuilder();
        sb.append("----ISO MESSAGE-----" + "\n");
        try {
            sb.append("  MTI : " + msg.getMTI() + "\n");
            sb.append(logFields(msg, ""));
        } catch (ISOException e) {
            e.printStackTrace();
        } finally {
            sb.append("--------------------");
        }
        return sb.toString();

    }

    private static String logFields(final ISOMsg isoMsg, final String prefix)
            throws ISOException {
        StringBuilder sb = new StringBuilder();

        for (int i = 1; i < isoMsg.getMaxField() + 1; i++) {

            if (isoMsg.hasField(i)) {

                sb.append(prefix + "Field-"
                        + isoMsg.getComponent(i).getKey().toString());
                sb.append(" : \"");

                if (isoMsg.getComponent(i).getValue() instanceof byte[]) {
                    sb.append(Hex.encodeHexString(
                            (byte[]) isoMsg.getComponent(i).getValue())
                            .toString()
                            + "\"\n");
                } else {
                    sb.append(isoMsg.getComponent(i).getValue().toString()
                            + "\"\n");
                }
                // has Subfields
                if (isoMsg.getComponent(i).getMaxField() > 0) {
                    sb.append(logFields((ISOMsg) isoMsg.getComponent(i),
                            "\tSub"));
                }
            }
        }
        return sb.toString();
    }

    /**
     * Method to get PrintStream for redirect trace logging.
     * 
     * @return PrintStream
     */
    public static PrintStream createLoggingProxy() {
        return new PrintStream(System.out) {
            public void print(final String string) {
                logger.debug(string);
                if (logger.isTraceEnabled()) {
                    System.out.println(string);
                }
            }

            public void println(final String string) {
                logger.debug(string);
                if (logger.isTraceEnabled())
                    System.out.println(string);
            }
        };
    }

    /**
     * @param hextwoData
     * @return true if inputparam is valid hex
     */
    public static boolean isHex(final String hextwoData) {
        String hexPattern = "([A-Fa-f0-9])+$";
        Pattern pattern = Pattern.compile(hexPattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(hextwoData);
        return matcher.matches();
    }

    /**
     * @param fieldPath
     * @return true if fieldPath is valid FieldPath
     */
    public static boolean isFieldPath(final String fieldPath) {
        String fieldPathPattern = "\\d\\d?.?\\d?\\d?.?\\d?\\d?";
        Pattern pattern = Pattern.compile(fieldPathPattern,
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(fieldPath);
        return matcher.matches();
    }

    /**
     * @param isoMsg
     * @return the Message in String xml-format
     */
    public static String getISOMsgXml(ISOMsg isoMsg) {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory
                .newInstance();

        try {
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("ISOMSG");
            rootElement.setAttribute("mti", isoMsg.getMTI());
            // root elements
            doc.appendChild(rootElement);

            for (int i = 1; i < isoMsg.getMaxField() + 1; i++) {
                if (isoMsg.hasField(i)) {
                    Element fieldElement = doc.createElement("FIELD"
                            + isoMsg.getComponent(i).getKey().toString());

                    if (isoMsg.getComponent(i).getValue() instanceof byte[]) {
                        fieldElement.setTextContent(Hex.encodeHexString(
                                (byte[]) isoMsg.getComponent(i).getValue())
                                .toString());
                    } else {
                        fieldElement.setTextContent(isoMsg.getComponent(i)
                                .getValue().toString());
                    }
                    // has Subfields
                    if (isoMsg.getComponent(i).getMaxField() > 0) {
                        // sb.append(logFields((ISOMsg) isoMsg.getComponent(i),
                        // "\tSub"));
                    }
                    rootElement.appendChild(fieldElement);
                }


            }
//                 Output to console for testing
//                 StreamResult result = new StreamResult(System.out);
            // write the content into xml
            TransformerFactory transformerFactory = TransformerFactory
                    .newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            String output = writer.getBuffer().toString().replaceAll("\n|\r", "");
//            String output = writer.getBuffer().toString() ;
            
            
            
            return output;

        } catch (ParserConfigurationException e) {
            logger.error("ParserConfigurationException  ", e);
        } catch (TransformerException e) {
            logger.error("TransformerException  ", e);
        } catch (ISOException e) {
            logger.error("ISOException  ", e);
        }
        return null;
    }
}
