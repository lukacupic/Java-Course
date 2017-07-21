package hr.fer.zemris.java.hw06.shell;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A utility class for performing some common tasks for the
 * {@link hr.fer.zemris.java.hw06.shell.ShellCommand} classes.
 *
 * @author Luka Čupić
 */
public class CommandUtil {

    /**
     * Returns a read-only list of strings created from one or more passed strings.
     *
     * @param strings the string(s) to store in the list
     * @return an un-modifiable list of passed string(s)
     */
    public static List<String> stringsToList(String... strings) {
        return Collections.unmodifiableList(Arrays.asList(strings));
    }

    /**
     * Trims the given array of strings by removing spaces
     * from beginning and end of each string from the array.
     *
     * @param array the array to trim
     * @return a trimmed array of strings
     */
    public static String[] trimStringArray(String[] array) {
        String[] newArray = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i].trim();
        }
        return newArray;
    }

    /**
     * Parses a given string into an array of arguments.
     * Arguments are splitted by (multiple) spaces; if an
     * argument is surrounded by quotation marks, then whole
     * content is parsed into a single argument, together with
     * all the spaces in the argument.
     *
     * @param string the string to parse
     * @return an array of arguments, splitted from the {@param string}
     * @throws ParseException if the string could not have been parsed
     */
    public static String[] parseArguments(String string) throws ParseException {
        List<String> argsList = new ArrayList<>();

        boolean parsingQuotes = false;

        StringBuilder currentArg = new StringBuilder();
        for (int i = 0, len = string.length(); i <= len; i++) {
            String currentString = currentArg.toString();

            if (i == len) {
                if (!currentString.isEmpty()) {
                    argsList.add(currentString);
                }
                break;
            }

            char c = string.charAt(i);
            if (c == '"') {
                if (parsingQuotes && !(i + 1 == len || string.charAt(i + 1) == ' ')) {
                    throw new ParseException("Illegal character found!", i + 1);
                }
                parsingQuotes = !parsingQuotes;
                continue;
            }
            if (c == ' ' && !parsingQuotes) {
                i += skipSpaces(string, i + 1);

                if (!currentString.isEmpty()) {
                    if (string.charAt(i - 1) == '"') {
                        currentString = escapeSequences(currentString);
                    }
                    argsList.add(currentString);
                    currentArg = new StringBuilder();
                }
                continue;
            }
            currentArg.append(c);
        }

        String[] argsArray = new String[argsList.size()];
        return argsList.toArray(argsArray);
    }

    /**
     * Parses the escape sequences of the given string and returns the
     * escaped string.
     *
     * @param currentString the string to escape
     * @return the escaped string
     */
    private static String escapeSequences(String currentString) {
        return currentString.replace("\\\"", "\"").replace("\\\\", "\\");
    }

    /**
     * Skips consecutive spaces in string {@param str}, starting
     * from the position {@param index}.
     *
     * @return the number of spaces skipped
     */
    private static int skipSpaces(String str, int index) {
        int counter = 0;
        for (int i = index, len = str.length(); i < len; i++) {
            if (str.charAt(i) != ' ') break;
            counter++;
        }
        return counter;
    }

    /**
     * Returns a string filled with {@param length} {@param ch} characters.
     *
     * @param length the number of characters to be stored in the string.
     * @param ch     the character to be stored in the string.
     * @return a string with {@param length} spaces.
     */
    public static String getChars(int length, char ch) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(ch);
        }
        return sb.toString();
    }

    /**
     * Splits the given string into substrings of length {@param length}.
     * The length of the given string must be divisible by the given length
     * of the substrings, otherwise the method will throw an instance of
     * {@link IllegalArgumentException}.
     *
     * @param string the length of each substring
     * @return an array of substrings of length {@param length}
     * @throws IllegalArgumentException if the given string cannot be splitted
     *                                  into substrings of the given length.
     */
    public static String[] splitToParts(String string, int length) {
        int strLen = string.length();

        if (strLen % length != 0) {
            throw new IllegalArgumentException("The given string cannot be splitted" +
                " into substrings of the given length.");
        }

        String[] parts = new String[strLen / length];
        for (int i = 0; i <= strLen - length; i += length) {
            parts[i / length] = string.substring(i, i + length);
        }
        return parts;
    }
}