package hr.fer.zemris.java.gui.calc;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.function.Function;

/**
 * The {@link Calculator} class represents a simple GUI calculator
 * which operates with a basic set of mathematical functions.
 * <p>
 * The calculator can also work in the "inverse" mode, which allows
 * calculations of inverse operations for some functions, such as
 * trigonometric functions, logarithm functions etc.
 *
 * @author Luka Čupić
 */
public class Calculator extends JFrame {

    /**
     * Represents the first operand of a binary operations,
     * and also, the main operand for all other types of
     * operations.
     */
    private double operand1;

    /**
     * Represents the second operand of a binary operation;
     * not relevant for other types of operations.
     */
    private double operand2;

    /**
     * Represents the current binary operator, if such exists.
     */
    private String operator;

    /**
     * Represents the value of the result currently displayed
     * on the calculator screen.
     */
    private double result;

    /**
     * Represents the screen of the calculator.
     */
    private JLabel screen;

    /**
     * Represents all the digit buttons (0-9).
     */
    private JButton[] digits;

    /**
     * Represents buttons for arithmetic operators, such as
     * addition, subtraction etc. All operators are essentially
     * binary operations, since they operate on exactly two
     * operands.
     */
    private JButton[] operators;

    /**
     * Represents buttons for different calculator operations,
     * such as clearing the screen.
     */
    private AbstractButton[] operations;

    /**
     * Represents buttons for performing functions such as
     * trigonometric functions. All functions are essentially
     * unary operations, since they operate on a single operand.
     */
    private JButton[] functions;

    /**
     * Represents buttons for miscellaneous operations, such as
     * sign-changing.
     */
    private JButton[] misc;

    /**
     * Represents the stack for storing result values.
     */
    private Stack<Double> stack;

    /**
     * A flag which tells whether the calculator is in the "inverse"
     * state.
     */
    private boolean isInverse;

    /**
     * A flag which tells whether the currently active operand is
     * a decimal value.
     */
    private boolean isDecimal;

    /**
     * Represents the counter of digits after the decimal dot,
     * for the currently active operand.
     */
    private int decimalCounter;

    /**
     * The constructor.
     */
    public Calculator() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(420, 300);
        setVisible(true);
        setTitle("Epic Calculator v1.0");

        initSettings();
        initGUI();
    }

    /**
     * A helper method for initializing the GUI.
     */
    private void initGUI() {
        JPanel p = CalcUtil.initCalc(this);
        add(p);
    }

    /**
     * A helper method for initializing the calculator settings.
     */
    private void initSettings() {
        stack = new Stack<>();
        isInverse = false;
        isDecimal = false;
    }

    /**
     * A helper method for updating the current (binary) operator.
     *
     * @param s the value of the new operator
     */
    protected void updateOperator(String s) {
        if (operator != null) {
            performOperation();
        }
        operator = s;
        decimalCounter = 0;
        isDecimal = false;
    }

    /**
     * Performs the operation specified by the current value
     * of the {@link #operator} object. The operation
     * is performed on {@link #operand1} and {@link #operand2}.
     */
    protected void performOperation() {
        switch (operator) {
            case "+":
                result = operand1 + operand2;
                break;
            case "-":
                result = operand1 - operand2;
                break;
            case "*":
                result = operand1 * operand2;
                break;
            case "/":
                result = operand1 / operand2;
                break;
            case "x^n":
                result = Math.pow(operand1, operand2);
                break;
            case "(inv)x^n":
                result = Math.pow(operand1, 1 / operand2);
                break;
        }
        operand1 = result;
        operand2 = 0;
        operator = null;
        updateScreen();
    }

    /**
     * Applies the specified function to the currently active operand.
     *
     * @param func the function to apply
     */
    protected void applyFunction(Function<Double, Double> func) {
        result = func.apply(currentOperand());
        operand1 = result;
        operand2 = 0;
        updateScreen();
    }

    /**
     * Updates the screen with the newest result.
     */
    private void updateScreen() {
        String str = (int) result == result ? String.valueOf((int) result) : String.valueOf(result);
        screen.setText(str);

        // why not implement an easter egg :)
        if (str.equals("42")) {
            JOptionPane.showMessageDialog(
                this,
                "You have discovered the answer to life, the universe and everything!",
                "Congratulations!",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    /**
     * Updates the value of the current operand.
     *
     * @param number the new value of the operand
     */
    private void updateOperand(double number) {
        if (operator == null) {
            operand1 = number;
            result = operand1;
        } else {
            operand2 = number;
            result = operand2;
        }
        updateScreen();
    }

    /**
     * Updates the currently active operand by adding the specified
     * number to the end of the current value of the operand.
     *
     * @param number the value to add to the currently active operand.
     */
    protected void appendToOperand(double number) {
        if (!isDecimal) {
            updateOperand(10 * currentOperand() + number);
        } else {
            updateOperand(currentOperand() + number / Math.pow(10, ++decimalCounter));
        }
    }

    /**
     * Updates the sign of the currently active operand.
     */
    protected void updateOperandSign() {
        updateOperand(-currentOperand());
    }

    /**
     * Clears the currently active operand, i.e. sets it's value to zero.
     */
    protected void clear() {
        updateOperand(0);
    }

    /**
     * Returns the value of the  currently active operand (i.e. the
     * one currently being displayed on the screen).
     *
     * @return the value of the currently active operand (i.e. the
     * one currently being displayed on the screen).
     */
    private double currentOperand() {
        return operator == null ? operand1 : operand2;
    }

    /**
     * Resets the calculator, putting it into it's initial state.
     */
    protected void reset() {
        operand1 = 0;
        operand2 = 0;
        result = 0;
        operator = null;
        stack.clear();
        updateScreen();
    }

    /**
     * Pushes the value of the currently active operand onto the
     * stack.
     */
    public void push() {
        stack.push(currentOperand());
    }

    /**
     * Pops the value from the top of the stack onto the screen,
     * thus replacing the previous value (both on the screen and
     * physically, in the memory).
     */
    public void pop() {
        if (stack.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Empty stack!",
                "Warning",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        updateOperand(stack.pop());
    }

    // Getters and setters

    /**
     * Gets the current screen.
     *
     * @return the current screen
     */
    public JLabel getScreen() {
        return screen;
    }

    /**
     * Gets the current array of digit buttons.
     *
     * @return the current array of digit buttons
     */
    public JButton[] getDigits() {
        return digits;
    }

    /**
     * Gets the current array of operator buttons.
     *
     * @return the current array of operator buttons
     */
    public JButton[] getOperators() {
        return operators;
    }

    /**
     * Gets the current array of operation buttons.
     *
     * @return the current array of operation buttons
     */
    public AbstractButton[] getOperations() {
        return operations;
    }

    /**
     * Gets the current array of function buttons.
     *
     * @return the current array of function buttons
     */
    public JButton[] getFunctions() {
        return functions;
    }

    /**
     * Gets the current array of miscellaneous buttons.
     *
     * @return the current array of miscellaneous buttons
     */
    public JButton[] getMisc() {
        return misc;
    }

    /**
     * Gets all buttons contained in the layout of this
     * calculator.
     *
     * @return all buttons contained in the layout of this
     * calculator
     */
    public AbstractButton[] getButtons() {
        List<AbstractButton> list = new ArrayList<>();
        list.addAll(Arrays.asList(digits));
        list.addAll(Arrays.asList(operators));
        list.addAll(Arrays.asList(operations));
        list.addAll(Arrays.asList(functions));
        list.addAll(Arrays.asList(misc));
        return list.toArray(new AbstractButton[0]);
    }

    /**
     * Creates a new screen.
     *
     * @param screen the new screen
     */
    public void setScreen(JLabel screen) {
        this.screen = screen;
    }

    /**
     * Creates a new empty array for the digits buttons.
     *
     * @param digits the new of digit buttons
     */
    public void setDigits(JButton[] digits) {
        this.digits = digits;
    }

    /**
     * Creates a new empty array for the operators buttons.
     *
     * @param operators the new array of operator buttons
     */
    public void setOperators(JButton[] operators) {
        this.operators = operators;
    }

    /**
     * Creates a new empty array for the operators buttons.
     *
     * @param operations the new array of operation buttons
     */
    public void setOperations(AbstractButton[] operations) {
        this.operations = operations;
    }

    /**
     * Creates a new empty array for the functions buttons.
     *
     * @param functions the new array of function buttons
     */
    public void setFunctions(JButton[] functions) {
        this.functions = functions;
    }

    /**
     * Creates a new empty array for the miscellaneous buttons.
     *
     * @param misc the new array of miscellaneous buttons
     */
    public void setMisc(JButton[] misc) {
        this.misc = misc;
    }

    /**
     * Checks if this calculator is currently in the "inverse" mode.
     *
     * @return true if the calculator is in the "inverse" mode; false otherwise
     */
    public boolean isInverse() {
        return isInverse;
    }

    /**
     * Sets the "inverse" flag.
     *
     * @param isInverse the new value for the flag
     */
    public void setInverse(boolean isInverse) {
        this.isInverse = isInverse;
    }

    /**
     * Checks if the currently active operand is of decimal type.
     *
     * @return true if the currently active operand is a decimal value;
     * false otherwise
     */
    public boolean isDecimal() {
        return isDecimal;
    }

    /**
     * Sets the flag which specifies if the currently active operand is
     * of decimal type.<
     *
     * @param decimal the new value for the flag
     */
    public void setDecimal(boolean decimal) {
        isDecimal = decimal;
    }

    // End of getters and setters

    /**
     * The main method.
     *
     * @param args command line arguments; not used in this program
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Calculator::new);
    }
}
