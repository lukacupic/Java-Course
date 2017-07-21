package hr.fer.zemris.bf.utils;

import hr.fer.zemris.bf.model.*;

import java.util.*;
import java.util.function.Consumer;

/**
 * This is a utility class which contains static methods for performing
 * some common tasks for the {@link NodeVisitor} objects.
 *
 * @author Luka Čupić
 */
public class Util {

    /**
     * Visits the given child by calling the {@link NodeVisitor#visit}
     * method of the appropriate node type.
     *
     * @param child the child to visit
     */
    public static void visitChild(NodeVisitor visitor, Node child) {
        if (child instanceof ConstantNode) {
            visitor.visit((ConstantNode) child);
            return;
        }

        if (child instanceof VariableNode) {
            visitor.visit((VariableNode) child);
            return;
        }

        if (child instanceof UnaryOperatorNode) {
            visitor.visit((UnaryOperatorNode) child);
            return;
        }

        if (child instanceof BinaryOperatorNode) {
            visitor.visit((BinaryOperatorNode) child);
        }
    }

    /**
     * Generates a "truth table" for the given list of variables. Each
     * row is represented by a boolean array whose consumption is delegated
     * to the given consumer object.
     *
     * @param variables the list of variables to process
     * @param consumer  the consumer to process the boolean arrays
     */
    public static void forEach(List<String> variables, Consumer<boolean[]> consumer) {
        int columns = variables.size();
        int rows = (int) Math.pow(2, columns);

        for (int i = 0; i < rows; i++) {
            boolean[] booleans = new boolean[columns];

            String s = Integer.toBinaryString(i);
            while (s.length() != columns) {
                s = '0' + s;
            }

            char[] chars = s.toCharArray();
            for (int j = 0; j < chars.length; j++) {
                booleans[j] = chars[j] == '1';
            }

            consumer.accept(booleans);
        }
    }

    /**
     * Evaluates all possible combinations of values of the given variables
     * (i.e. all rows of the truth table, which has 2^n rows, where n is the
     * number of variables) and returns a set of such combinations of values
     * for which the result of the expression is equal to the given boolean
     * expression value.
     *
     * @param variables       the variables
     * @param expression      the root node of the tree model representing
     *                        the expression
     * @param expressionValue the expression to check all combinations with
     * @return a set of distinct combinations of values of the given variables
     * which satisfy, i.e. are equal to the provided boolean expression.
     */
    public static Set<boolean[]> filterAssignments(
        List<String> variables, Node expression, boolean expressionValue) {
        List<boolean[]> rows = new ArrayList<>();
        Util.forEach(variables, rows::add);

        ExpressionEvaluator eval = new ExpressionEvaluator(variables);
        Set<boolean[]> set = new TreeSet<>((o1, o2) -> 1);

        for (boolean[] row : rows) {
            eval.setValues(row);
            expression.accept(eval);
            if (eval.getResult() == expressionValue) {
                set.add(row);
            }
        }
        return set;
    }

    /**
     * Receives an array of boolean values and converts it into the ordinal
     * number (i.e. index), starting from zero, of the combination from the
     * truth table in which the given array represents a combination.
     * <p>
     * For example, if the given array is: [false, false, true, true], the
     * return result will be <code>3</code>.
     *
     * @param values the array of boolean values representing a combination
     *               of the truth table, or -1 if the given combination was
     *               not found in the truth table
     * @return the index of the given combination from the truth table
     */
    public static int booleanArrayToInt(boolean[] values) {
        List<boolean[]> rows = new ArrayList<>();
        Util.forEach(Arrays.asList(new String[values.length]), rows::add);

        for (int i = 0, size = rows.size(); i < size; i++) {
            boolean[] row = rows.get(i);
            if (Arrays.equals(row, values)) return i;
        }
        return -1;
    }

    /**
     * Returns indexes of those truth table combinations whose result
     * of the given expression evaluates to <code>true</code>.
     *
     * @param variables  the variables of the truth table
     * @param expression the expression to evaluate the combinations
     * @return a set of integers representing the minterms of the truth
     * table
     */
    public static Set<Integer> toSumOfMinterms(
        List<String> variables, Node expression) {
        return getIndexes(variables, expression, true);
    }

    /**
     * Returns indexes of those truth table combinations whose result
     * of the given expression evaluates to <code>false</code>.
     *
     * @param variables  the variables of the truth table
     * @param expression the expression to evaluate the combinations
     * @return a set of integers representing the maxterms of the truth
     * table
     */
    public static Set<Integer> toProductOfMaxterms(
        List<String> variables, Node expression) {
        return getIndexes(variables, expression, false);
    }

    /**
     * A helper method which returns indexes of those truth table
     * combinations whose result of the given expression evaluates
     * to {@param value}.
     *
     * @param variables  the variables of the truth table
     * @param expression the expression to evaluate the combinations
     * @param value      the value which will be used for evaluation
     * @return a set of integers representing the indexes of those
     * combinations which satisfy the given value.
     * table
     */
    private static Set<Integer> getIndexes(
        List<String> variables, Node expression, Boolean value) {
        Set<boolean[]> set = Util.filterAssignments(variables, expression, value);

        Set<Integer> i = new TreeSet<>();
        for (boolean[] c : set) {
            i.add(Util.booleanArrayToInt(c));
        }
        return i;
    }
}
