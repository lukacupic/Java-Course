package hr.fer.zemris.java.hw02;

import java.text.NumberFormat;
import java.text.ParseException;

/**
 * Represents a complex number with various methods for managing complex numbers.
 * The canonical basis of a complex number is represented by it's real and imaginary parts.
 *
 * @author Luka Čupić
 */
public class ComplexNumber {

    /**
     * Represents the immutable real part of the complex number.
     */
    private final double real;

    /**
     * Represents the immutable imaginary part of the complex number.
     */
    private final double imaginary;

    /**
     * Sets up the complex number.
     *
     * @param real      the real part of the complex number.
     * @param imaginary the imaginary part of the complex number.
     */
    public ComplexNumber(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    /**
     * Returns a new complex number with only the real part.
     *
     * @param real the real value of the new complex number.
     * @return a new complex number with only the real part.
     */
    public static ComplexNumber fromReal(double real) {
        return new ComplexNumber(real, 0);
    }

    /**
     * Returns a new complex number with only the imaginary part.
     *
     * @param imaginary the imaginary value of the new complex number.
     * @return a new complex number with only the imaginary part.
     */
    public static ComplexNumber fromImaginary(double imaginary) {
        return new ComplexNumber(0, imaginary);
    }

    /**
     * Returns a new complex number in the canonical basis.
     *
     * @param magnitude the magnitude of the complex number. Must be a non-negative number.
     * @param angle     the angle of the complex number.
     * @return a new complex number in the canonical basis.
     * @throws IllegalArgumentException if magnitude is not a legal value.
     */
    public static ComplexNumber fromMagnitudeAndAngle(double magnitude, double angle) {
        if (magnitude < 0) {
            throw new IllegalArgumentException();
        }

        double x = magnitude * Math.cos(angle);
        double y = magnitude * Math.sin(angle);

        return new ComplexNumber(x, y);
    }

    /**
     * Returns a new complex number parsed from the string parameter.
     *
     * @param s a string representation of the complex number.
     * @return a new complex number parsed from the string parameter.
     * @throws IllegalArgumentException if the passed string cannot be parsed as a complex number.
     */
    public static ComplexNumber parse(String s) throws IllegalArgumentException {

        double real;
        double imaginary;

        // formatter will be used for parsing numbers
        NumberFormat formatter = NumberFormat.getInstance();

        String realString;
        String imaginaryString;

        // string is only the real part
        if (!s.contains("i")) {
            realString = s;
            imaginaryString = null;

            // string is a whole complex number, or just the imaginary part
        } else {
            // find the '+' and '-' indexes, ignoring the 0th place (sign)
            int plusIndex = s.indexOf("+", 1);
            int minusIndex = s.indexOf("-", 1);

            String parts[];

            // if '+' index exists, complex number has both parts; split it by '+'
            if (plusIndex != -1) {
                parts = s.split("\\+");

                realString = parts[0];
                imaginaryString = parts[1];

                // if '-' index exists, the complex number has both parts; split it by '-'
            } else if (minusIndex != -1) {
                parts = s.split("\\-");

                realString = !parts[0].isEmpty() ? parts[0] : parts[1];
                imaginaryString = !parts[0].isEmpty() ? parts[1] : parts[2];

                // since we've splitted by '-', the imaginary part must be negative
                imaginaryString = "-" + imaginaryString;

                // if the splitter ignored the negative sign of the real part, put it back
                if (s.charAt(0) == '-') {
                    realString = "-" + realString;
                }

                // if no index exists, then the complex number has only the imaginary part
            } else {
                realString = null;
                imaginaryString = s;
            }
        }

        if (realString == null) {
            real = 0;
        } else {
            try {
                real = formatter.parse(realString).doubleValue();
            } catch (ParseException e) {
                throw new IllegalArgumentException();
            }
        }

        if (imaginaryString == null) {
            imaginary = 0;
        } else if (imaginaryString.charAt(0) == 'i') {
            imaginary = 1;
        } else if (imaginaryString.charAt(0) == '-' && imaginaryString.charAt(1) == 'i') {
            imaginary = -1;
        } else {
            try {
                imaginary = formatter.parse(imaginaryString).doubleValue();
            } catch (ParseException e) {
                throw new IllegalArgumentException();
            }
        }

        return new ComplexNumber(real, imaginary);
    }

    /**
     * Adds the passed complex number to this one and returns a new complex number as the result.
     *
     * @param c the complex number to add to this one.
     * @return the sum of this complex number and the argument.
     */
    public ComplexNumber add(ComplexNumber c) {
        double realTotal = this.real + c.getReal();
        double imagTotal = this.imaginary + c.getImaginary();

        return new ComplexNumber(realTotal, imagTotal);
    }

    /**
     * Subtracts the passed complex number from this one and returns a new complex number as the result.
     *
     * @param c the complex number to subtract from this one.
     * @return the difference of this complex number and the argument.
     */
    public ComplexNumber sub(ComplexNumber c) {
        double realTotal = this.real - c.getReal();
        double imagTotal = this.imaginary - c.getImaginary();

        return new ComplexNumber(realTotal, imagTotal);
    }

    /**
     * Multiplies the passed complex number with this one and returns a new complex number as the result.
     *
     * @param c the complex number to multiply to this one.
     * @return the product of this complex number and the argument.
     */
    public ComplexNumber mul(ComplexNumber c) {
        double realTotal = this.real * c.getReal() - this.imaginary * c.getImaginary();
        double imagTotal = this.real * c.getImaginary() + this.imaginary * c.getReal();

        return new ComplexNumber(realTotal, imagTotal);
    }

    /**
     * Divides this complex number by the passed complex number and returns a new complex number as the result.
     *
     * @param c the complex number to divide this one by. Must be a non-zero complex number.
     * @return the quotient of diving this complex number by the argument.
     * @throws IllegalArgumentException if the division cannot be completed due to illegal argument.
     */
    public ComplexNumber div(ComplexNumber c) {
        if (c.getReal() == 0 && c.getImaginary() == 0) {
            throw new IllegalArgumentException();
        }

        double realTotal = this.real * c.getReal() + this.imaginary * c.getImaginary();
        double imagTotal = this.imaginary * c.getReal() - this.real * c.getImaginary();

        double denom = Math.pow(c.getReal(), 2) + Math.pow(c.getImaginary(), 2);

        realTotal /= denom;
        imagTotal /= denom;

        return new ComplexNumber(realTotal, imagTotal);
    }

    /**
     * Calculates the n-th power of this complex number and returns the result as the a new complex number.
     *
     * @param n the exponent to raise the complex number to. Must be a non-negative number.
     * @return the n-th power of this complex number.
     * @throws IllegalArgumentException if the argument is a negative number.
     */
    public ComplexNumber power(int n) throws IllegalArgumentException {
        if (n < 0) {
            throw new IllegalArgumentException();
        }

        double newAngle = n * getAngle();
        double newMagnitude = Math.pow(getMagnitude(), n);

        double realTotal = newMagnitude * Math.cos(newAngle);
        double imagTotal = newMagnitude * Math.sin(newAngle);

        return new ComplexNumber(realTotal, imagTotal);
    }

    /**
     * Calculates all the n-th roots of this complex number and returns the solutions
     * in the form of an array of complex numbers.
     *
     * @param n the root to take from the complex number. Must be greater than zero.
     * @return the n-th root of this complex number.
     * @throws IllegalArgumentException if the argument is not greater than zero.
     */
    public ComplexNumber[] root(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }

        ComplexNumber[] roots = new ComplexNumber[n];

        double newMagnitude = Math.pow(getMagnitude(), 1.0 / n);

        for (int k = 0; k < n; k++) {
            double newAngle = (getAngle() + 2 * k * Math.PI) / n;

            double real = newMagnitude * Math.cos(newAngle);
            double imag = newMagnitude * Math.sin(newAngle);

            roots[k] = new ComplexNumber(real, imag);
        }

        return roots;
    }

    /**
     * Returns the real part of this complex number.
     *
     * @return the real part of this complex number.
     */
    public double getReal() {
        return this.real;
    }

    /**
     * Returns the imaginary part of this complex number.
     *
     * @return the imaginary part of this complex number.
     */
    public double getImaginary() {
        return this.imaginary;
    }

    /**
     * Returns the magnitude of this complex number.
     *
     * @return the magnitude of this complex number.
     */
    public double getMagnitude() {
        return Math.sqrt(this.real * this.real + this.imaginary * this.imaginary);
    }

    /**
     * Returns the angle of this complex number from 0 to 2pi.
     *
     * @return the angle of this complex number from 0 to 2pi.
     */
    public double getAngle() {
        double angle = Math.atan2(imaginary, real);

        if (imaginary < 0) {
            angle += 2 * Math.PI;
        }

        return angle;
    }

    /**
     * Returns a string representation of this complex number.
     *
     * @return a string representation of this complex number.
     */
    public String toString() {
        String real = String.format("%.4f", this.real);
        String imag = String.format("%.4fi", Math.abs(this.imaginary));

        real += this.imaginary < 0 ? " + " : " - ";

        return real + imag;
    }
}
