package hr.fer.zemris.java.hw06.crypto;

/**
 * This class offers static methods {@link Util#bytetohex(byte[])} which
 * converts an array of bytes into a hexadecimal string representation and
 * {@link Util#hextobyte(String)} (byte[])} which converts a hexadecimal
 * value, represented by a string, into an array of bytes.
 *
 * @author Luka Čupić
 */
public class Util {

	/**
	 * Converts a given hexadecimal value, represented by a string,
	 * into an array of bytes.
	 *
	 * @param keyText the hexadecimal string to convert.
	 * @return the converted array of bytes.
	 */
	public static byte[] hextobyte(String keyText) {
		if (keyText == null || keyText.length() % 2 != 0) {
			throw new IllegalArgumentException("The passed string is not valid!");
		}
		if (keyText.length() == 0) return new byte[0];

		char[] array = keyText.toLowerCase().toCharArray();
		byte[] bytes = new byte[keyText.length() / 2];

		for (int i = 0; i <= array.length - 2; i += 2) {
			byte b = (byte) Integer.parseInt(array[i] + "" + array[i + 1], 16);
			bytes[i / 2] = b;
		}
		return bytes;
	}

	/**
	 * Converts a given array of bytes into a string representation
	 * of a hexadecimal number.
	 *
	 * @param bytearray the array of bytes to convert.
	 * @return a string representation of the hexadecimal number.
	 */
	public static String bytetohex(byte[] bytearray) {
		if (bytearray.length == 0) return "";

		StringBuilder total = new StringBuilder();

		for (byte b : bytearray) {
			// get the binary value of the byte
			String binaryString = Integer.toBinaryString((int) b);

			// if the value is negative, take the last 8 out of int's 32 bits
			if ((int) b < 0) {
				binaryString = binaryString.substring(24, 32);
			}

			// convert the binary string into a hex string
			int bin = Integer.parseInt(binaryString, 2);
			String hex = Integer.toString(bin, 16);

			// add the leading zero if missing
			if (hex.length() == 1) hex = "0" + hex;

			total.append(hex);
		}
		return total.toString();
	}
}
