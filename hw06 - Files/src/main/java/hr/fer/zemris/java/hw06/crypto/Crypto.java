package hr.fer.zemris.java.hw06.crypto;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Scanner;

/**
 * This class allows encryption and decryption of a given file using the
 * AES cryptographic algorithm and offers calculation and verification of
 * the SHA-256 file digest.
 * <p>
 * These operations are performed by providing command line arguments.
 * The first argument must be one of the following:
 * <p>
 * {@code checksha}, {@code encrypt} or {@code decrypt}
 * <p>
 * If a digest is being checked, then one more argument is required: the path
 * to the file whose digest is to be checked. If encryption or decryption is
 * performed, then two arguments are required: the original file and the file
 * which will store the encrypted/decrypted data.
 * <p>
 * Checking of a file's digest is performed in the following way:
 * <p>
 * {@code
 * checksha <file>
 * }
 * <p>
 * where {@code <file>} is the name of the file whose digest is to be checked.
 * <p>
 * Encryption of a file is performed in the following way:
 * <p>
 * {@code
 * encrypt <original_file> <encrypted_file>
 * }
 * <p>
 * where {@code <original_file>} is the name of the original file to encrypt
 * and {@code <encrypted_file>} is the name of the file which will contain the
 * encrypted data.
 * <p>
 * Decryption of a file is performed in the following way:
 * <p>
 * {@code
 * decrypt <original_file> <decrypted_file>
 * }
 * <p>
 * where {@code <original_file>} is the name of the file to decrypt and
 * {@code <decrypted_file>} is the name of the file which will contain the
 * decrypted data.
 *
 * @author Luka Čupić
 */
public class Crypto {

	/**
	 * The main method.
	 *
	 * @param args command line arguments.
	 */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		if (args.length == 2) {
			if (args[0].equals("checksha")) {
				try {
					checkSHA(args[1], sc);
				} catch (Exception e) {
					System.out.println("Could not digest the file!");
					return;
				}
			} else {
				System.out.println("Unknown command!");
				return;
			}
		} else if (args.length == 3) {
			if (args[0].equals("encrypt")) {
				try {
					encrypt(args[1], args[2], sc);
				} catch (Exception e) {
					System.out.println("Could not encrypt the file!");
					return;
				}

			} else if (args[0].equals("decrypt")) {
				try {
					decrypt(args[1], args[2], sc);
				} catch (Exception e) {
					System.out.println("Could not decrypt the file!");
					return;
				}

			} else {
				System.out.println("Unknown command!");
				return;
			}
		} else {
			System.out.println("Illegal number of arguments!");
			return;
		}

		sc.close();
	}

	/**
	 * Checks if the digest provided by the user (through the standard input)
	 * is equal to the digest of a given file.
	 *
	 * @param filename the file whose digest will be compared against the user's.
	 * @param sc       a scanner for reading from the standard input.
	 */
	private static void checkSHA(String filename, Scanner sc) {
		// read all bytes from the file
		byte[] fileBytes;
		try {
			fileBytes = Files.readAllBytes(Paths.get(filename));
		} catch (IOException ex) {
			throw new RuntimeException();
		}

		// calculate the file digest
		MessageDigest sha;
		try {
			sha = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException ex) {
			throw new RuntimeException();
		}
		sha.update(fileBytes);

		// get the digest bytes as string
		byte[] digestBytes = sha.digest();
		String digestString = Util.bytetohex(digestBytes);

		// read the user's digest string
		System.out.printf("Please provide expected SHA-256 digest for %s:%n> ", filename);
		String inputDigest = sc.nextLine();

		// compare the strings and write a message to the user
		boolean equal = inputDigest.equals(digestString);

		System.out.printf(
			"%nDigesting completed. Digest of %s %s expected digest.",
			filename,
			equal ? "matches" : "does not match"
		);

		if (!equal) {
			System.out.printf(" Digest was: %s%n", digestString);
		} else {
			System.out.printf("%n");
		}
	}

	/**
	 * Encrypts a file using the AES algorithm, and stores the encrypted data into
	 * a new file.
	 *
	 * @param originalFile  the file to decrypt.
	 * @param encryptedFile the file to store the decrypted data to.
	 * @param sc            a scanner for reading from the standard input.
	 * @throws IOException if an input/output error occurs while performing
	 *                     the encryption upon the file
	 */
	private static void encrypt(String originalFile, String encryptedFile, Scanner sc) throws IOException {
		crypt(originalFile, encryptedFile, true, sc);
	}

	/**
	 * Decrypts the original file using the AES algorithm, and stores the decrypted data
	 * into a new file.
	 *
	 * @param originalFile  the file to decrypt.
	 * @param decryptedFile the file to store the decrypted data to.
	 * @param sc            a scanner for reading from the standard input.
	 * @throws IOException if an input/output error occurs while performing
	 *                     decryption upon the file
	 */
	private static void decrypt(String originalFile, String decryptedFile, Scanner sc) throws IOException {
		crypt(originalFile, decryptedFile, false, sc);
	}

	/**
	 * A helper method for performing either encryption or decryption of a file, denoted by the {@param encrypt}.
	 *
	 * @param originalFile the file to encrypt/decrypt.
	 * @param cryptedFile  the file to store the encrypted/decrypted data to.
	 * @param encrypt      a boolean for selecting whether the file should be encrypted (true)
	 *                     or decrypted (false).
	 * @param sc           a scanner for reading from the standard input.
	 * @throws IOException if an input/output error occurs while performing
	 *                     encryption or decryption upon the file(s).
	 */
	private static void crypt(String originalFile, String cryptedFile, boolean encrypt, Scanner sc) throws IOException {
		System.out.printf("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):%n> ");
		String password = sc.nextLine();

		System.out.printf("Please provide initialization vector as hex-encoded text (32 hex-digits):%n> ");
		String vector = sc.nextLine();

		// initializes the cipher
		Cipher cipher = initCipher(password, vector, encrypt);

		// does the actual encryption/decryption
		doTheCrypting(cipher, originalFile, cryptedFile);

		// print the message to the user
		System.out.printf(
			"%n%s completed. Generated file %s based on file %s.",
			encrypt ? "Encryption" : "Decryption",
			cryptedFile,
			originalFile
		);
	}

	/**
	 * A helper method for initializing the cipher by choosing the encryption/decryption
	 * algorithm and by doing some other super-secret stuff.
	 *
	 * @param password the encryption/decryption password.
	 * @param vector   the encryption/decryption initialization vector.
	 * @param encrypt  a boolean for selecting whether the file should be encrypted (true)
	 *                 or decrypted (false).
	 * @return a new, initialized cipher.
	 */
	private static Cipher initCipher(String password, String vector, boolean encrypt) {
		SecretKeySpec keySpec = new SecretKeySpec(Util.hextobyte(password), "AES");
		AlgorithmParameterSpec paramSpec = new IvParameterSpec(Util.hextobyte(vector));

		Cipher cipher;
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec, paramSpec);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		return cipher;
	}

	/**
	 * A helper method which does the actual reading of bytes and writes the encrypted/decrypted
	 * data into a new file.
	 *
	 * @param cipher       the cipher which is used to encrypt/decrypt the original file.
	 * @param originalFile the file to encrypt/decrypt.
	 * @param cryptedFile  the file to store the encrypted/decrypted data to.
	 * @throws IOException if an input/output error occurs while performing
	 *                     encryption or decryption upon the file(s).
	 */
	private static void doTheCrypting(Cipher cipher, String originalFile, String cryptedFile) throws IOException {
		// initialize the source and the drain
		BufferedInputStream in;
		BufferedOutputStream out;
		try {
			in = new BufferedInputStream(new FileInputStream(originalFile));
			out = new BufferedOutputStream(new FileOutputStream(cryptedFile));
		} catch (FileNotFoundException e) {
			throw new IOException(e);
		}

		// reads a chunk of bytes from the input, performs a crypting operation and then writes
		// the crypted chunk to the output; do these steps until there's no chunks left
		while (true) {
			// the buffer for storing a chunk of bytes
			byte[] buffer = new byte[4096];

			int value = in.read(buffer);
			if (value == -1) break;

			byte[] result = cipher.update(buffer);
			out.write(result);
		}

		// flush any remaining bytes and close the streams
		out.flush();
		in.close();
		out.close();
	}
}