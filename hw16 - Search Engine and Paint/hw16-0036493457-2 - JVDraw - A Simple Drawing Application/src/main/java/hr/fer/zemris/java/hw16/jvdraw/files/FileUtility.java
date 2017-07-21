package hr.fer.zemris.java.hw16.jvdraw.files;

import hr.fer.zemris.java.hw16.jvdraw.list.dialog.DialogUtil;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.List;

/**
 * This class represents a utility class which offers static
 * methods for some common tasks.
 *
 * @author Luka Čupić
 */
public class FileUtility {

	/**
	 * A filter for the JVD file format.
	 */
	private static FileFilter JVDFilter = new FileFilter() {
		@Override
		public boolean accept(File f) {
			return f.toString().toLowerCase().endsWith(".jvd");
		}

		@Override
		public String getDescription() {
			return "JVD (.*jvd)";
		}
	};

	/**
	 * A filter for the JPG, PNG and GIF file formats.
	 */
	private static FileFilter imageFilter = new FileFilter() {
		@Override
		public boolean accept(File f) {
			String file = f.toString().toLowerCase();
			if (file.endsWith(".jpg")) return true;
			if (file.endsWith(".png")) return true;
			if (file.endsWith(".gif")) return true;
			return false;
		}

		@Override
		public String getDescription() {
			return "Image File (.*jpg, *.png, *.gif)";
		}
	};

	/**
	 * Converts a specified string array into an integer array,
	 * considering all items from the given array are parsable
	 * to integers.
	 *
	 * @param array the array to convert to integer array
	 * @return the integer array of the given string array
	 */
	public static Integer[] toIntArray(String[] array) {
		Integer[] integers = new Integer[array.length];

		for (int i = 0; i < array.length; i++) {
			integers[i] = Integer.parseInt(array[i]);
		}
		return integers;
	}

	/**
	 * Converts the given list of strings into a single string
	 * object, where the previous strings are separated
	 * by the line separators (new line characters).
	 *
	 * @param list the list to convert to string
	 * @return a string representation of the specified list
	 */
	public static String toString(List<String> list) {
		StringBuilder sb = new StringBuilder();

		for (String s : list) {
			sb.append(s).append(System.lineSeparator());
		}
		return sb.toString();
	}

	/**
	 * Returns a filter for the {@code *.jvd} file format.
	 *
	 * @return a filter for the {@code *.jvd} file format.
	 */
	public static FileFilter getJVDFilter() {
		return JVDFilter;
	}

	/**
	 * Returns a filter for the {@code *.jpg, *.png and *.gif}
	 * file formats.
	 *
	 * @return a filter for the {@code *.jpg, *.png and *.gif}
	 * file formats.
	 */
	public static FileFilter getImageFilter() {
		return imageFilter;
	}
}
