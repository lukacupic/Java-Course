package hr.fer.zemris.hw18.util;

/**
 * A utility class.
 *
 * @author Luka Čupić
 */
public class Utility {

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
}