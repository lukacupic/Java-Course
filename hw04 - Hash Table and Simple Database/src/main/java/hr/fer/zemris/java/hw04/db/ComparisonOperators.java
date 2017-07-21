package hr.fer.zemris.java.hw04.db;

/**
 * Represents comparison operators for lexicographically comparing two strings.
 *
 * @author Luka Čupić
 */
public class ComparisonOperators {

    /**
     * Checks whether the first string is lexicographically smaller than the second string.
     */
    public static final IComparisonOperator LESS = (value1, value2) -> value1.compareTo(value2) < 0;

    /**
     * Checks whether the first string is lexicographically smaller than or equal to the second string.
     */
    public static final IComparisonOperator LESS_OR_EQUAL = (value1, value2) -> value1.compareTo(value2) <= 0;

    /**
     * Checks whether the first string is lexicographically greater than the second string.
     */
    public static final IComparisonOperator GREATER = (value1, value2) -> value1.compareTo(value2) > 0;

    /**
     * Checks whether the first string is lexicographically smaller than or equal to the second string.
     */
    public static final IComparisonOperator GREATER_OR_EQUAL = (value1, value2) -> value1.compareTo(value2) >= 0;

    /**
     * Checks whether the first string is lexicographically equal to the second string.
     */
    public static final IComparisonOperator EQUALS = (value1, value2) -> value1.compareTo(value2) == 0;

    /**
     * Checks whether the first string is lexicographically different from the second string.
     */
    public static final IComparisonOperator NOT_EQUAL = (value1, value2) -> value1.compareTo(value2) != 0;

    /**
     * Checks whether the first string satisfies the pattern given by the second string.
     * The second string can hold a wildcard character, denoted by an asterisk (*), which
     * can represent any character from the first string.
     */
    public static final IComparisonOperator LIKE = (value1, value2) -> {
        // get the number of wildcard characters in the second string
        int noOfWildcards = value2.length() - value2.replace("*", "").length();

        if (noOfWildcards > 1) {
            throw new IllegalArgumentException(
                "The pattern in the second string must contain at most one wildcard character!"
            );
        }

        // if there's no wildcard, just compare the strings
        if (noOfWildcards == 0) {
            return value1.equals(value2);
        }

        String[] parts = value2.split("\\*");

        // if the wildcard is at the end
        if (parts.length == 1) {
            return value1.startsWith(parts[0]);
        }

        // if the wildcard is at the beginning
        if (parts[0].equals("")) {
            return value1.endsWith(parts[1]);
        }

        // if it wasn't neither of the previous two, then it's in the middle
        return value1.startsWith(parts[0]) && value1.endsWith(parts[1]);
    };
}
