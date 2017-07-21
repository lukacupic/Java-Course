package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 * This class allows a user to calculate the area and the circumference of a rectangle.
 *
 * @author Luka Čupić
 */
public class Rectangle {

    /**
     * The beginning method of the class.
     *
     * @param args attributes of the rectangle (width and height)
     */
    public static void main(String[] args) {

        double width = 0, height = 0;
        String output = "Pravokutnik širine %.1f i visine %.1f ima površinu %.1f te opseg %.1f.%n";

        if (args.length != 0 && args.length != 2) {
            System.out.println("Neispravan unos, doviđenja!");
        } else if (args.length == 0) {
            Scanner sc = new Scanner(System.in);

            width = readNumber(sc, "Unesite širinu > ");
            height = readNumber(sc, "Unesite visinu > ");

            sc.close();

        } else if (args.length == 2) {
            try {
                width = Double.parseDouble(args[0]);
                height = Double.parseDouble(args[1]);
            } catch (NumberFormatException ex) {
                System.out.println("Neispravni argumenti, 'đenja!");
                System.exit(1);
            }
        }

        System.out.printf(
                output,
                width,
                height,
                calculateArea(width, height),
                calculateCircumference(width, height)
        );
    }

    /**
     * Reads and returns a decimal number from the standard input. If the input cannot
     * be interpreted as a decimal number, the method will continue with inquiry until the
     * input becomes valid.
     *
     * @param sc      standard input scanner
     * @param message the message to be displayed if the input is incorrect.
     * @return the number read by the scanner.
     */
    public static double readNumber(Scanner sc, String message) {
        double value = 0;

        System.out.printf(message);
        do {
            String input = sc.next();
            try {
                value = Double.parseDouble(input);

                if (value < 0) {
                    System.out.println("Unijeli ste negativnu vrijednost!");
                    System.out.printf(message);
                } else {
                    break;
                }
            } catch (NumberFormatException ex) {
                System.out.printf("'%s' se ne može protumačiti kao broj!%n", input);
                System.out.printf(message);
            }

        } while (sc.hasNext());
        return value;
    }

    /**
     * Calculates the area of a rectangle.
     *
     * @param width  width of the rectangle.
     * @param height height of the rectangle.
     * @return the area of the rectangle.
     */
    public static double calculateArea(double width, double height) {
        return width * height;
    }

    /**
     * Calculates the circumference of a rectangle.
     *
     * @param width  width of the rectangle.
     * @param height height of the rectangle.
     * @return the circumference of the rectangle.
     */
    public static double calculateCircumference(double width, double height) {
        return 2 * (width + height);
    }
}