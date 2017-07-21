package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.ObjectStack;

/**
 * A simple demonstration of the ObjectStack collection which evaluates the postfix
 * notation of a mathematical operation. The program accepts a single argument: the
 * expression which should be evaluted, wrapped inside quotation marks. All other
 * forms of input are considered invalid.
 * <p>
 * Example of usage:
 * "8 2 /" will be evaluated to 4.
 *
 * @author Luka Čupić
 */
public class StackDemo {

    /**
     * The main method of the class.
     *
     * @param args a single argument, which represents the expression.
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Invalid input!");
            System.exit(1);
        }

        String[] elements = args[0].split(" ");

        ObjectStack stack = new ObjectStack();

        for (String element : elements) {
            // if the element is a number (an operand)
            try {
                int num = Integer.parseInt(element);
                stack.push(num);

                // else, the element is a char (an operator)
            } catch (NumberFormatException ex) {
                try {
                    int op2 = (int) stack.pop();
                    int op1 = (int) stack.pop();
                    char op = element.charAt(0);

                    // if division by zero is to occur
                    if (op2 == 0 && op == '/') {
                        System.err.println("Cannot divide by zero!");
                        System.exit(1);
                    }

                    int result = performOperation(op1, op2, op);
                    stack.push(result);

                    // if something bad happens, it's probably user's fault
                } catch (Exception exc) {
                    System.err.println("Invalid expression!");
                    System.exit(1);
                }
            }
        }

        if (stack.size() != 1) {
            System.err.println("An error has occured. Please check your input.");
        } else {
            System.out.println("Expression evaluates to " + stack.pop() + ".");
        }
    }

    /**
     * Performs a mathematical operation between two of the operands.
     *
     * @param operand1  the first operand
     * @param operand2  the second operand
     * @param operation the operation to be performed.
     * @return value of the operation.
     */
    private static int performOperation(int operand1, int operand2, char operation) {
        int result = 0;

        switch (operation) {
            case '+':
                result = operand1 + operand2;
                break;
            case '-':
                result = operand1 - operand2;
                break;
            case '*':
                result = operand1 * operand2;
                break;
            case '/':
                result = operand1 / operand2;
                break;
            case '%':
                result = operand1 % operand2;
                break;
        }
        return result;
    }
}