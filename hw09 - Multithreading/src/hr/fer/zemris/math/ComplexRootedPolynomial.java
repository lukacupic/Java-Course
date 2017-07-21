package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a complex-rooted polynomial of complex numbers.
 * The polynomial is represented by the following expression:
 * {@code f(z) = (z-z1)*(z-z2)*...*(z-zn)}
 * where f(z) is a function whose domain and codomain are both
 * complex numbers, meaning that it takes a single argument z,
 * where z iz a complex number, and returns another complex
 * number. Values z1, ..., zn are the roots of the polynomial.
 * The returned complex number is the value of the polynomial
 * at the specified point(i.e. complex number, since complex
 * numbers represent points of a complex polynomial).
 *
 * @author Luka Čupić
 */
public class ComplexRootedPolynomial {

    /**
     * Represents the roots of this complex number.
     */
    private List<Complex> roots;

    /**
     * Creates a new instance of this class.
     *
     * @param roots the roots of this polynomial
     */
    public ComplexRootedPolynomial(Complex... roots) {
        this.roots = new ArrayList<>(Arrays.asList(roots));
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
        Complex result = new Complex(1, 0);
        for (Complex root : roots) {
            result = result.multiply(z.sub(root));
        }
        return result;
    }

    /**
     * Converts this representation of a polynomial of complex
     * numbers into a {@link ComplexPolynomial} representation
     * of a polynomial of complex numbers.
     *
     * @return a {@link ComplexPolynomial} representation of a
     * polynomial of complex numbers
     */
    public ComplexPolynomial toComplexPolynomial() {
        List<Complex> roots = new ArrayList<>();

        // negate all roots to make the multiplication simpler
        for (Complex root : this.roots) {
            roots.add(root.negate());
        }

        // convert each factor to a complex polynomial
        List<ComplexPolynomial> factors = new ArrayList<>();
        for (Complex root : roots) {
            // (z+zi) becomes a polynomial zi*z^0 + 1*z^1
            factors.add(new ComplexPolynomial(root, Complex.ONE));
        }

        // multiply the polynomials
        ComplexPolynomial product = new ComplexPolynomial(Complex.ONE);
        for (ComplexPolynomial p : factors) {
            product = product.multiply(p);
        }
        return product;
    }

    /**
     * Finds a root of this polynomial which is closest to
     * the specified complex number and returns it's index.
     * The distance between the specified complex number and
     * a root is limited by providing a threshold. If no roots
     * are found within the given threshold, {@code -1} is
     * returned.
     *
     * @param z         the complex number
     * @param threshold the maximum distance between the given
     *                  complex number and a polynomial root
     * @return the index of a root closest to the specified
     * complex number that is within the given threshold, or
     * {@code -1} if no roots are found within the threshold
     */
    public int indexOfClosestRootFor(Complex z, double threshold) {
        Complex closestRoot = null;
        double shortestDistance = -1;

        for (Complex root : roots) {
            double dist = z.sub(root).module();

            if (dist > threshold) continue;

            if (dist < shortestDistance || shortestDistance == -1) {
                shortestDistance = dist;
                closestRoot = root;
            }
        }
        return roots.contains(closestRoot) ? roots.indexOf(closestRoot) : -1;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Complex root : roots) {
            sb.append(String.format("[z - (%s)]*", root.toString()));
        }
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }
}