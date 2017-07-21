package hr.fer.zemris.java.hw12;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * This is a utility class for some of the classes in this project.
 * The {@link Utility} class contains static methods used to perform
 * miscellaneous tasks.
 *
 * @author Luka Čupić
 */
public class Utility {

	/**
	 * Reads the file specified by the given path.
	 *
	 * @param path a string representation of the path to the file
	 * @return a string, representing the entire contents of the file
	 * @throws IOException if the file could not have been read
	 */
	public static String readFromFile(String path) throws IOException {
		try {
			byte[] data = Files.readAllBytes(Paths.get(path));
			return new String(data, StandardCharsets.UTF_8);
		} catch (Exception ex) {
			throw new IOException(ex);
		}
	}
}
