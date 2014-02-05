package com.wirecard.acqp.two;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
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

public final class MsgUtils {
	private static Logger logger = LoggerFactory.getLogger(MsgUtils.class);

	public static String GetBitMap(String input) {
		return input.substring(8, 24);
	}

	public static String GetData(String input) {
		// return (input.substring(24, 226));
		return stripFs(input.substring(24, 226));
	}

	public static byte[] decodeNibbleHex(String input) {
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

	static String hexStringToBinaryString(String HexString) throws ISOException {
		StringBuilder sb = new StringBuilder();
		char[] chars = HexString.toCharArray();

		for (int i = 0; i < chars.length - 1; i++) {
			String hex = "" + chars[i] + chars[i + 1];
			i++;
			sb.append(hexToBinary(hex));

		}

		return sb.toString();
	}

	static String hexToBinary(String Hex) throws ISOException {
		int i = Integer.parseInt(Hex, 16);
		String Bin = Integer.toBinaryString(i);
		return LeftPadder.ZERO_PADDER.pad(Bin, 8);
	}

	public static String cleanNibblesFromHexMsg(String hexMsg) {
		StringBuilder sb = new StringBuilder();
		int tempBegin = 0;
		String part;
		int couter = 0;
		Matcher matcher = Pattern.compile("(F[A-F0-9]){2,}").matcher(hexMsg);

		while (matcher.find()) {
			System.out.printf("%s an Position [%d,%d]%n", matcher.group(),
					matcher.start(), matcher.end());

			if (tempBegin < matcher.start()) {
				System.out.println("noFs "
						+ hexMsg.substring(tempBegin, matcher.start()));
				sb.append(hexMsg.substring(tempBegin, matcher.start()));
			}

			part = hexMsg.substring(matcher.start(), matcher.end()).replace(
					"F", "");
			System.out.println("counter: " + couter + " " + part);
			sb.append(part);

			tempBegin = matcher.end();
			couter++;
		}

		System.out.println(sb.toString());
		return sb.toString();
	}

	public static String hextoASCII(String hex) {
		StringBuilder output = new StringBuilder();
		for (int i = 0; i < hex.length(); i += 2) {
			String str = hex.substring(i, i + 2);

			System.out.println(str);
			output.append((char) (Integer.parseInt(str, 16)));
		}

		return output.toString();

	}

	static void logISOMsgPlainText(ISOMsg msg) {
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
		System.out.println(sb);

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

	public static void logISOHeader(String bASE1Header) {
		System.out.println("----ISO HEADER-----");
		try {
			// String header = ISOUtil.hexString(bASE1Header.pack());
			StringBuilder header = new StringBuilder();
			String lf = System.getProperty("line.separator");
			StringBuffer d = new StringBuffer();

			d.append(lf);
			d.append("[H 01] ");
			d.append(header.substring(0, 2));
			d.append(lf);
			d.append("[H 02] ");
			d.append(header.substring(2, 4));
			d.append(lf);
			d.append("[H 03] ");
			d.append(header.substring(4, 6));
			d.append(lf);
			d.append("[H 04] ");
			d.append(header.substring(6, 10));
			d.append(lf);
			d.append("[H 05] ");
			d.append(header.substring(10, 16));
			d.append(lf);
			d.append("[H 06] ");
			d.append(header.substring(16, 22));
			d.append(lf);
			d.append("[H 07] ");
			d.append(header.substring(22, 24));
			d.append(lf);
			d.append("[H 08] ");
			d.append(header.substring(24, 28));
			d.append(lf);
			d.append("[H 09] ");
			d.append(header.substring(28, 34));
			d.append(lf);
			d.append("[H 10] ");
			d.append(header.substring(34, 36));
			d.append(lf);
			d.append("[H 11] ");
			d.append(header.substring(36, 42));
			d.append(lf);
			d.append("[H 12] ");
			d.append(header.substring(42, 44));
			d.append(lf);
			System.out.println(d.toString());

		} finally {
			System.out.println("--------------------");
		}
		;
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

}
