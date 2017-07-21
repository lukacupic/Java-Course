package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a complex number, and offers methods for performing
 * different operations upon complex numbers. Each complex number ir represented
 * by it's real and imaginary parts.
 * <p>
 * <p>
 * An instance of this class is immutable, meaning that it cannot be modified
 * once it's created. This also means that all operations performed on a
 * {@link Complex} object return a new object which represents the result
 * of the operation.
 *
 * @author Luka Čupić
 */
public class Complex {

    /**
     * Represents zero.
     */
    public static final Complex ZERO = new Complex(0, 0);

    /**
     * Represents a real number {@code 1}.
     */
    public static final Complex ONE = new Complex(1, 0);

    /**
     * Represents a real number {@code -1}.
     */
    public static final Complex ONE_NEG = new Complex(-1, 0);

    /**
     * Represents an imaginary number i.
     */
    public static final Complex IM = new Complex(0, 1);

    /**
     * Represents an imaginary number -i.
     */
    public static final Complex IM_NEG = new Complex(0, -1);

    /**
     * Represents the real component of this complex number.
     */
    private double re;

    /**
     * Represents the imaginary component of this complex number.
     */
    private double im;

    /**
     * The default constructor.
     */
    public Complex() {
    }

    /**
     * Creates a new complex number.
     *
     * @param re real component
     * @param im imaginary component
     */
    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    /**
     * The euclidean distance of this complex number from the
     * origin of the complex plane.
     *
     * @return the module of this complex number.
     */
    public double module() {
        return Math.sqrt(re * re + im * im);
    }

    /**
     * Adds the specified complex number to this complex number.
     *
     * @param c the complex number to add
     * @return the result of the addition operation between this
     * complex number and the specified complex number
     */
    public Complex add(Complex c) {
        double re = this.re + c.getReal();
        double im = this.im + c.getImaginary();

        return new Complex(re, im);
    }

    /**
     * Subtracts the specified complex number from this complex number.
     *
     * @param c the complex number to subtract by
     * @return the result of the subtraction operation between this
     * complex number and the specified complex number
     */
    public Complex sub(Complex c) {
        double re = this.re - c.getReal();
        double im = this.im - c.getImaginary();

        return new Complex(re, im);
    }

    /**
     * Multiplies this complex number by the specified complex number.
     *
     * @param c the complex number to multiply by
     * @return the result of the multiplication operation between this
     * complex number and the specified complex number
     */
    public Complex multiply(Complex c) {
        double re = this.re * c.getReal() - this.im * c.getImaginary();
        double im = this.re * c.getImaginary() + this.im * c.getReal();

        return new Complex(re, im);
    }

    /**
     * Divides this complex number by the specified complex number.
     *
     * @param c the complex number to divide by
     * @return the result of the division operation between this
     * complex number and the specified complex number
     */
    public Complex divide(Complex c) {
        if (c.getReal() == 0 && c.getImaginary() == 0) {
            throw new IllegalArgumentException("Cannot divide by zero!");
        }

        double re = this.re * c.getReal() + this.im * c.getImaginary();
        double im = this.im * c.getReal() - this.re * c.getImaginary();

        double cRe = c.getReal();
        double cIm = c.getImaginary();
        double denom = cRe * cRe + cIm * cIm;

        re /= denom;
        im /= denom;

        return new Complex(re, im);
    }

    /**
     * Returns a new complex number which represents the additive
     * inverse of this complex number.
     *
     * @return a complex number whose real and imaginary parts
     * are opposite to those of this complex number
     */
    public Complex negate() {
        return new Complex(-re, -im);
    }

    /**
     * Raises this complex number to the n-th power and returns
     * a new complex number representing it.
     *
     * @param n the power to raise this complex number to
     * @return a new complex number representing this complex number
     * raised to the n-th power
     */
    public Complex power(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Cannot compute negative power!");
        }

        double angle = n * angle();
        double module = Math.pow(module(), n);

        double realTotal = module * Math.cos(angle);
        double imagTotal = module * Math.sin(angle);

        return new Complex(realTotal, imagTotal);
    }

    /**
     * Returns all n-th roots of this complex number.
     *
     * @param n the root to take
     * @return a list of all n-th roots of this complex number
     */
    public List<Complex> root(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Cannot take root smaller than one!");
        }

        List<Complex> roots = new ArrayList<>();

        double module = Math.pow(module(), 1.0 / n);
        for (int i = 0; i < n; i++) {
            double angle = (angle() + 2 * Math.PI * i) / n;

            double re = module * Math.cos(angle);
            double im = module * Math.sin(angle);

            roots.add(new Complex(re, im));
        }
        return roots;
    }

    /**
     * Parses the given complex number in the form of a string
     * and returns a {@link Complex} object representing the
     * parsed complex number.
     *
     * @param string the string to parse
     * @return a {@link Complex} object representing the
     * parsed complex number
     * @throws NumberFormatException if the string does not
     *                               contain a parsable number
     */
    public static Complex parse(String string) {
        string = string.replace(" ", "");

        if (!string.contains("i")) {
            double re = Double.parseDouble(string);
            return new Complex(re, 0);
        }

        String[] parts = string.split("i");

        double im = (parts.length < 2) ? 1 : Double.parseDouble(parts[1]);

        if (parts.length == 0 || parts[0].isEmpty()) {
            return new Complex(0, im);
        }

        String real = parts[0];
        int len = real.length();

        char sign = real.charAt(len - 1);
        if (sign != '+' && sign != '-') {
            throw new NumberFormatException("Invalid sign operator!");
        }
        double re = len != 1 ? Double.parseDouble(real.substring(0, len - 1)) : 0;
        return new Complex(re, sign == '+' ? im : -im);
    }

    /**
     * Returns the angle of this complex number from 0 to 2pi.
     *
     * @return the angle of this complex number from 0 to 2pi.
     */
    public double angle() {
        return Math.atan2(im, re);
    }

    /**
     * Returns the real component of this complex number.
     *
     * @return the real component of this complex number.
     */
    public double getReal() {
        return re;
    }

    /**
     * Returns the imaginary component of this complex number.
     *
     * @return the imaginary component of this complex number.
     */
    public double getImaginary() {
        return im;
    }

    @Override
    public String toString() {
        String re = String.format("%.6f", this.re);
        String im = String.format("%.6fi", Math.abs(this.im));

        return re + (this.im < 0 ? " - " : " + ") + im;
    }
}
