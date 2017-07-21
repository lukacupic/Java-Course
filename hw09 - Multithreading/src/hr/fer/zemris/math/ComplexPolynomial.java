package hr.fer.zemris.math;

import java.util.*;

/**
 * Represents a polynomial of complex numbers.
 * The polynomial is represented by the following expression:
 * {@code f(z) = z0 + z1*z + z2*z^2 + ... + zn*z^n}
 * where f(z) is a function whose domain and codomain are both
 * complex numbers, meaning that it takes a single argument z,
 * where z iz a complex number, and returns another complex
 * number. Values z1, ..., zn are the factors of the polynomial.
 * The returned complex number is the value of the polynomial
 * at the specified point(i.e. complex number, since complex
 * numbers represent points of a complex polynomial).
 *
 * @author Luka Čupić
 */
public class ComplexPolynomial {

    /**
     * Represents a real number 1, wrapped in a {@link ComplexPolynomial}
     * object.
     */
    public static final ComplexPolynomial ONE = new ComplexPolynomial(
        new Complex(1, 0)
    );

    /**
     * Represents the factors of this complex number.
     */
    private List<Complex> factors;

    /**
     * Creates a new instance of this class by receiving
     * an arbitrary number of factors for this polynomial.
     * Factors are indexed from {@code 0} to {@code n},
     * meaning that the first factor given to the constructor
     * will represent a coefficient of the term with the lowest
     * power ({@code z^0}), where the last factor will represent
     * a coefficient of the term with the highest power ({@code z^n}).
     *
     * @param factors the factors of this polynomial
     */
    public ComplexPolynomial(Complex... factors) {
        this.factors = new ArrayList<>(Arrays.asList(factors));
    }

    /**
     * Returns the degree of this polynomial.
     *
     * @return the degree of this polynomial
     */
    public short order() {
        return (short) (factors.size() - 1);
    }

    /**
     * Multiples this polynomial by the specified polynomial.
     *
     * @param p the polynomial to multiply by
     * @return a new {@link ComplexPolynomial}, representing
     * the result of the multiplication operation
     */
    public ComplexPolynomial multiply(ComplexPolynomial p) {
        List<Complex> mFactors = new LinkedList<>();

        for (int i = 0, size1 = factors.size(); i < size1; i++) {
            Complex factor1 = factors.get(i);

            for (int j = 0, size2 = p.getFactors().size(); j < size2; j++) {
                Complex factor2 = p.getFactors().get(j);

                Complex mul = factor1.multiply(factor2);
                int degree = i + j;

                if (degree >= mFactors.size()) {
                    mFactors.add(degree, mul);
                } else {
                    Complex updated = mul.add(mFactors.get(degree));
                    mFactors.remove(degree);
                    mFactors.add(degree, updated);
                }
            }
        }
        return new ComplexPolynomial(mFactors.toArray(new Complex[0]));
    }

    /**
     * Computes the first derivative of this polynomial.
     * <p>
     * For example, calling this method for a polynomial
     * {@code (7+2i)z^3+2z^2+5z+1} will return the value
     * {@code (21+6i)z^2+4z+5}.
     *
     * @return a new {@link ComplexPolynomial} object,
     * representing the the first derivative of this
     * polynomial
     */
    public ComplexPolynomial derive() {

        List<Complex> dFactors = new ArrayList<>();
        for (int i = 1, size = factors.size(); i < size; i++) {
            Complex factor = factors.get(i);
            dFactors.add(factor.multiply(new Complex(i, 0)));
        }
        return new ComplexPolynomial(dFactors.toArray(new Complex[0]));
    }

    /**
     * Computes the value of this polynomial at the given
     * point.
     *
     * @param z the point to compute this polynomial at
     * @return the value of this polynomial at the given
     * point
     */
    public Complex apply(Complex z) {
        Complex result = Complex.ZERO;
        for (int i = 0, size = factors.size(); i < size; i++) {
            Complex factor = factors.get(i);
            result = result.add(z.power(i).multiply(factor));
        }
        return result;
    }

    /**
     * Returns the factors of this polynomial.
     *
     * @return an unmodifiable list of factors of this
     * polynomial
     */
    public List<Complex> getFactors() {
        return Collections.unmodifiableList(factors);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0, size = factors.size(); i < size; i++) {
            Complex factor = factors.get(i);
            sb.append(String.format("(%s)*z^%d + ", factor.toString(), i));
        }
        sb.delete(sb.length() - 3, sb.length());

        return sb.toString();
    }
}