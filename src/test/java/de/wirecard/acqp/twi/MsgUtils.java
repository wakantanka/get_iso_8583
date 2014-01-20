package de.wirecard.acqp.twi;

import java.io.ByteArrayOutputStream;

import org.jpos.iso.ISOException;
import org.jpos.iso.LeftPadder;

public final class MsgUtils {

	public static String GetMTI(String input) {
		return stripFs(input.substring(0, 8));
	}

	public static 	String GetBitMap(String input) {
		// System.out.println(ISOUtil.trimf(testdata.substring(8, 24)));
		return input.substring(8, 24);
	}
	

	public static String GetData(String input) {
//		return (input.substring(24, 226));
		return stripFs(input.substring(24, 226));
	}

	private static byte[] decodeNibbleHex(String input) {
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
			if (input.charAt(i) != 'F' && i % 2 == 1)
				result.append(input.charAt(i));
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

}
