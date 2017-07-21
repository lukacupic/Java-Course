package hr.fer.zemris.math;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ComplexRootedPolynomialTest {

    @Test
    public void testApplyingAValue() {
        // http://www.wolframalpha.com/input/?i=((2%2B3i)-1)((2%2B3i)-2)

        ComplexRootedPolynomial p = new ComplexRootedPolynomial(
            new Complex(1, 0),
            new Complex(2, 0)
        );

        Complex result = p.apply(new Complex(2, 3));

        assertEquals(-9, result.getReal(), 10E-5);
        assertEquals(3, result.getImaginary(), 10E-5);
    }

    @Test
        public void testConvertingToComplexPolynomial() {
        ComplexRootedPolynomial p1 = new ComplexRootedPolynomial(
            new Complex(1, 0),
            new Complex(2, 0)
        );

        ComplexPolynomial p2 = p1.toComplexPolynomial();

        Complex value = new Complex(42, 56);

        Complex c1 = p1.apply(value);
        Complex c2 = p2.apply(value);

        assertEquals(c1.getReal(), c2.getReal(), 10E-5);
        assertEquals(c1.getImaginary(), c2.getImaginary(), 10E-5);
    }

    @Test
    public void testFindingClosestRoot() {
        ComplexRootedPolynomial p = new ComplexRootedPolynomial(
            new Complex(1.5, 2.3),
            new Complex(7.8, 9.14)
        );

        Complex number = new Complex(1.6, 2.2);
        int index = p.indexOfClosestRootFor(number, 0.5);

        assertEquals(0, index);
    }

    @Test
    public void testFindingClosestRootButWithTooSmallThreshold() {
        ComplexRootedPolynomial p = new ComplexRootedPolynomial(
            new Complex(1.5, 2.3),
            new Complex(7.8, 9.14)
        );

        Complex number = new Complex(1.6, 2.2);
        int index = p.indexOfClosestRootFor(number, 0.00042);

        assertEquals(-1, index);
    }

}