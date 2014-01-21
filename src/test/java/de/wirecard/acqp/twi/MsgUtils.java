package de.wirecard.acqp.twi;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jpos.iso.ISOException;
import org.jpos.iso.LeftPadder;

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
				System.out.println("noFs " + hexMsg.substring(tempBegin, matcher.start()));
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
	    for (int i = 0; i < hex.length(); i+=2) {
	        String str = hex.substring(i, i+2);
	        
	        System.out.println(str);
	        output.append((char) (Integer.parseInt(str, 16)));
	    }
	    
		return output.toString();
		
	}
}
