package demo;

import hr.fer.zemris.bf.model.Node;
import hr.fer.zemris.bf.parser.Parser;
import hr.fer.zemris.bf.qmc.Minimizer;
import hr.fer.zemris.bf.utils.Util;

import java.util.*;

/**
 * A demonstration program of the {@link Minimizer} class.
 *
 * @author Luka Čupić
 */
public class QMC {

    /**
     * Represents variables of the currently minimized function.
     */
    private static List<String> variables;

    /**
     * Represents minterms of the currently minimized function.
     */
    private static Set<Integer> minterms;

    /**
     * Represents don't cares of the currently minimized function.
     */
    private static Set<Integer> dontCares;

    /**
     * The main method.
     *
     * @param args command line arguments; not used in this program
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.printf("> ");
        while (sc.hasNextLine()) {

            String line = sc.nextLine();

            if (line.equals("quit")) {
                System.out.println("Goodbye!");
                break;
            }

            try {
                parseLine(line);
                minimizeFunction();
            } catch (Exception ex) {
                System.out.println("Pogreška: funkcija nije ispravno zadana.");
            }
            System.out.print("> ");
        }
    }

    /**
     * A helper method which minimizes the given function.
     */
    private static void minimizeFunction() {
        Minimizer minimizer;
        try {
            minimizer = new Minimizer(minterms, dontCares, variables);
        } catch (IllegalArgumentException ex) {
            System.out.println("Pogreška: skup minterma i don't careova nije disjunktan.");
            return;
        }
        List<String> minimalFormsAsString = minimizer.getMinimalFormsAsString();
        for (int i = 0; i < minimalFormsAsString.size(); i++) {
            System.out.printf("%d. %s %n", i + 1, minimalFormsAsString.get(i));
        }
    }

    /**
     * A helper method which parses the given line from the input.
     *
     * @param line the line to parse
     */
    private static void parseLine(String line) {
        String[] parts = line.split("=");

        readFunctionDefinition(parts[0]);
        readFunctionValues(parts[1]);
    }

    /**
     * A helper method which reads "values" of the function, where
     * "values" are minterms and don't cares.
     *
     * @param string the string to read the values from
     */
    private static void readFunctionValues(String string) {
        String[] parts = string.split("\\|");

        String mintermsString = parts[0].trim();
        minterms = readExpression(mintermsString);

        if (parts.length == 1) {
            dontCares = new LinkedHashSet<>();
            return;
        }

        String dontcaresString = parts[1].trim();
        dontCares = readExpression(dontcaresString);

    }

    /**
     * A helper method which reads the expression, which can be either a
     * boolean expression or a set of indexes.
     *
     * @param string the string to read
     * @return a set of indexes extracted from the given expression
     */
    private static Set<Integer> readExpression(String string) {
        if (string.startsWith("[")) {
            return readIndexes(string.substring(1, string.length() - 1));
        } else {
            return parseExpression(string);
        }
    }

    /**
     * Reads the indexes from the given expression.
     *
     * @param string the string to read
     * @return a set of indexes extracted from the given expression
     */
    private static Set<Integer> readIndexes(String string) {
        String[] indexesStr = trimStringArray(string.split(","));

        Set<Integer> indexes = new LinkedHashSet<>();
        for (String s : indexesStr) {
            indexes.add(Integer.parseInt(s));
        }
        return indexes;
    }

    /**
     * Reads the indexes from the given expression.
     *
     * @param string the string to read
     * @return a set of indexes extracted from the given expression
     */
    private static Set<Integer> parseExpression(String string) {
        Parser parser = new Parser(string);
        Node expression = parser.getExpression();

        return Util.toSumOfMinterms(variables, expression);
    }

    /**
     * Reads the left side of the function, i.e. it's definition.
     *
     * @param string the string to read
     */
    private static void readFunctionDefinition(String string) {
        if (!checkFunctionName(string)) {
            throw new IllegalArgumentException("Illegal function name!");
        }
        variables = getVariables(string);

        if (variables.size() == 0) {
            throw new IllegalArgumentException("Function must contain " +
                "at least one variable!");
        }
    }

    /**
     * Checks if the function name is a legal identifier.
     *
     * @param string the string to check
     * @return if the function name is legal; false otherwise
     */
    private static boolean checkFunctionName(String string) {
        String functionName = string.substring(0, string.indexOf("("));
        return isIdentifier(functionName.trim());
    }

    /**
     * Checks if the given string is an identifier. An identifier starts
     * with a letter and contains only letters and numbers.
     *
     * @param s the string to check
     * @return true if the given string is an identifier; false otherwise.
     */
    private static boolean isIdentifier(String s) {
        char[] chars = s.toCharArray();

        if (!Character.isLetter(chars[0])) return false;

        for (char c : chars) {
            if (Character.isLetterOrDigit(c)) continue;
            return false;
        }
        return true;
    }

    /**
     * Returns a list of variables extracted from the function's
     * definition.
     *
     * @param string the string to read
     * @return a list of variables extracted from the function's
     * definition
     */
    private static List<String> getVariables(String string) {
        int leftBracket = string.indexOf("(");
        int rightBracket = string.indexOf(")");

        String varString = string.substring(leftBracket + 1, rightBracket);
        String[] variables = varString.split(",");

        return Arrays.asList(trimStringArray(variables));
    }

    /**
     * Trims the given array of strings by removing spaces
     * from beginning and end of each string from the array.
     *
     * @param array the array to trim
     * @return array of trimmed strings
     */
    public static String[] trimStringArray(String[] array) {
        String[] newArray = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i].trim();
        }
        return newArray;
    }
}