package hr.fer.zemris.java.hw15.web;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class provides security features for the web application.
 * One such feature is hashing the user's password which will be
 * stored as the password identifier into the persistence context.
 *
 * @author Luka Čupić
 */
public class Security {

	/**
	 * Constructs an SHA-1 hash from the given password.
	 *
	 * @param password the password to hash
	 * @return the SHA-1 hash of the given password
	 */
	public static String hashPassword(String password) {
		byte[] bytes = password.getBytes();

		MessageDigest sha;
		try {
			sha = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException ex) {
			throw new RuntimeException("Unknown digesting algorithm!");
		}
		sha.update(bytes);

		byte[] digestBytes = sha.digest();
		return bytetohex(digestBytes);
	}

	/**
	 * Converts a given array of bytes into a string representation
	 * of a hexadecimal number.
	 *
	 * @param bytearray the array of bytes to convert
	 * @return a string representation of the hexadecimal number
	 */
	private static String bytetohex(byte[] bytearray) {
		if (bytearray.length == 0) return "";

		StringBuilder total = new StringBuilder();

		for (byte b : bytearray) {
			String binaryString = Integer.toBinaryString((int) b);

			if ((int) b < 0) {
				binaryString = binaryString.substring(24, 32);
			}

			int bin = Integer.parseInt(binaryString, 2);
			String hex = Integer.toString(bin, 16);

			if (hex.length() == 1) hex = "0" + hex;

			total.append(hex);
		}
		return total.toString();
	}
}
