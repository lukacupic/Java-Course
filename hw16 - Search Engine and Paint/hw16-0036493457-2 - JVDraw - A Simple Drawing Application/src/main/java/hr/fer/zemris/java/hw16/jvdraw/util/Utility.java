package hr.fer.zemris.java.hw16.jvdraw.util;

import javax.swing.*;

/**
 * This class is a utility class which offers static methods
 * for performing some commonly used tasks which are shared
 * among classes in the program.
 *
 * @author Luka Čupić
 */
public class Utility {

	/**
	 * Displays an error message with the specified {@param message}.
	 *
	 * @param message the message to be displayed
	 */
	public static void displayError(String message) {
		JOptionPane.showMessageDialog(null, message, "Error!", JOptionPane.ERROR_MESSAGE);
	}
}