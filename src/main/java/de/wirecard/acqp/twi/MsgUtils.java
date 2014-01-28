package de.wirecard.acqp.twi;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Hex;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.LeftPadder;
import org.jpos.iso.header.BASE1Header;

public final class MsgUtils {

	public static String GetMTI(String input) {
		return stripFs(input.substring(0, 8));
	}

	public static String GetBitMap(String input) {
		// System.out.println(ISOUtil.trimf(testdata.substring(8, 24)));
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

	// TODO move toUTils?
	static void logISOMsg(ISOMsg msg) {
		System.out.println("----ISO MESSAGE-----");
		try {
			System.out.println("  MTI : " + msg.getMTI());
			for (int i = 1; i <= msg.getMaxField(); i++) {
				if (msg.hasField(i)) {
					System.out.print("    Field-"
							+ msg.getComponent(i).getKey().toString() + " : \"" );
							 if ( msg.getComponent(i).getValue() instanceof byte[]) {
								 System.out.print( 	 Hex.encodeHexString((byte[]) msg.getComponent(i).getValue()).toString() + "\"\n" );
							}
							 else {
								 System.out.print( msg.getComponent(i).getValue().toString() + "\"\n" );
							}
	
					if (msg.getComponent(i).getMaxField() > 0) {
	
						for (int j = 1; j < msg.getComponent(i).getMaxField() + 1; j++) {
							ISOMsg isoSubMsg = (ISOMsg) msg.getComponent(i);
							if (isoSubMsg.hasField(j)) {
	
								System.out.println("        SubField-"
										+ (isoSubMsg.getComponent(j).getKey()
												.toString()
												+ " : \"" + isoSubMsg
												.getComponent(j).getValue()
												.toString()) + "\"");
							}
						}
					}
				}
			}
		} catch (ISOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("--------------------");
		}
	
	}

//	public static void logISOHeader(BASE1Header bASE1Header) {
	public static void logISOHeader(String bASE1Header) {
		System.out.println("----ISO HEADER-----");
		try {
//			String header = ISOUtil.hexString(bASE1Header.pack());
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
}
