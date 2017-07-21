package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 * This class can be used for calculating factorials of natural numbers
 * in the interval [1, 20].
 *
 * @author Luka Čupić
 */
public class Factorial {

    /**
     * This is the beginning method of the class.
     *
     * @param args command line arguments; not used in this program.
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.printf("Dobar dan.%nUnesite broj > ");

        while (sc.hasNext()) {
            String input = sc.next();

            if (input.compareTo("kraj") == 0) {
                break;
            }

            try {
                int num = Integer.parseInt(input);
                long fact = factorial(num);

                if (fact != -1) {
                    System.out.printf("%d! = %d%n", num, fact);
                } else {
                    System.out.printf("'%d' nije u dozvoljenom rasponu!%n", num);
                }
            } catch (NumberFormatException ex) {
                System.out.printf("'%s' nije cijeli broj!%n", input);
            }

            System.out.printf("Unesite broj > ");
        }

        System.out.printf("Doviđenja.%n");
        sc.close();
    }

    /**
     * Calculates factorial of ze given number.
     *
     * @param number the number to calculate the factorial of. Must be
     *               in the interval [0, 20].
     * @return the factorial of the given argument; -1 if the number is outside
     * the interval [0, 20].
     */
    public static long factorial(int number) {
        if (number < 0 || number > 20) {
            return -1;
        } else if (number == 1) {
            return 1;
        }
        return number * factorial(number - 1);
    }
}