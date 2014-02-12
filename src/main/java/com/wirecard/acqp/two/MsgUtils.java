package com.wirecard.acqp.two;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.codec.binary.Hex;
import org.jpos.iso.ISOComponent;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.LeftPadder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract  class MsgUtils {
	private static Logger logger = LoggerFactory.getLogger(MsgUtils.class);

	 static String GetMCBitMap(String input) {
		if (input.substring(24, 40).contains("F"))
		return input.substring(8, 24);
		else return input.substring(8, 40);
	}


	 static byte[] decodeNibbleHex(String input) {
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

	static String stripFs(String input) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < input.length(); i++) {

			if (i % 2 == 0 && input.charAt(i) != 'F')
				throw new NumberFormatException("Wrong Format for F-Nibbles");

			if (i % 2 == 1) {
				result.append(input.charAt(i));
			}
		}
		return result.toString();
	}

	static String stripAllFs(String input) {
		return stripAllFs(input, 0);
	}

	static String stripAllFs(String input, int offset) {
		StringBuilder result = new StringBuilder();
		result.append(input.substring(0, offset));
		for (int i = offset; i < input.length(); i++) {

			if (input.charAt(i) != 'F') {
				result.append(input.charAt(i));
			}
		}
		return result.toString();
	}

	static String getISOMsgPlainText(ISOMsg msg) {
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

	private static String logFields(ISOMsg isoMsg, String prefix)
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
				if (isoMsg.getComponent(i).getMaxField() > 0)
					sb.append(logFields((ISOMsg) isoMsg.getComponent(i),
							"\tSub"));
			}
		}
		return sb.toString();
	}

 
	public static PrintStream createLoggingProxy() {
		return new PrintStream(System.out) {
			public void print(final String string) {
				logger.debug(string);
				if (logger.isTraceEnabled())
					System.out.println(string);
				;
			}

			public void println(final String string) {
				logger.debug(string);
				if (logger.isTraceEnabled())
					System.out.println(string);
			}
		};
	}

	public static String logISOMsgXml() {

		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("ISOMsg");
			rootElement.setAttribute("mti", "0100");
			doc.appendChild(rootElement);

			Element field1 = doc.createElement("field-1");
			rootElement.appendChild(field1);

			Element field2 = doc.createElement("field-2");

			Element subfield21 = doc.createElement("SubField-1");
			// field2.appendChild(doc.createTextNode("yong"));
			field2.appendChild(subfield21);

			rootElement.appendChild(field2);
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);

			// Output to console for testing
			StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "<Field-3></Field-3>";
	}

	public static boolean isHex(String hextwoData) {
		   String hexPattern = "([A-Fa-f0-9])+$";
		   Pattern  pattern = Pattern.compile(hexPattern, Pattern.CASE_INSENSITIVE);
		   Matcher matcher = pattern.matcher(hextwoData);
			  return matcher.matches();
	}
	
	  static byte[] getBytesFromTwoDataMC(
			String twoInput)
					throws UnsupportedEncodingException, ISOException {
		String mti = new String(MsgUtils.decodeNibbleHex(twoInput
				.substring(0, 8)), "Cp1047");
		String bitmap = new String(MsgUtils.GetMCBitMap(twoInput));
		
		int dataOfsset = 24;
		if (bitmap.length()>16) //secondary Bit Map is present
			dataOfsset = 40;
		
		String dataPartMC = new String(MsgUtils.decodeNibbleHex(twoInput
				.substring(dataOfsset, twoInput.length())), "Cp1047");
		
		StringBuilder sb = new StringBuilder();
		sb.append(mti);
		sb.append(bitmap);
		sb.append(dataPartMC);
		
		String data = sb.toString();
		return (data.getBytes());
	}
	
	  static byte[] getBytesFromTwoDataJCB(
			String twoInput)
			throws UnsupportedEncodingException, ISOException {
		String mti = new String(MsgUtils.decodeNibbleHex(twoInput
				.substring(0, 8)), "Cp1047");
		String bitmap = new String(MsgUtils.GetMCBitMap(twoInput));

//		String dataPartJcb = new  String(twoInput				.substring(24, twoInput.length()) );
		String dataPartJcb =  new String(MsgUtils.stripAllFs(twoInput.substring(24, twoInput.length()),110));

		StringBuilder sb = new StringBuilder();
		sb.append(mti);
		sb.append(bitmap);
		sb.append(dataPartJcb);

		String data = sb.toString();
		return (data.getBytes());
	}


}
