package hr.fer.zemris.math;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ComplexPolynomialTest {

    @Test
    public void testMultiplyingPolynomials() {
        ComplexPolynomial p1 = new ComplexPolynomial(
            new Complex(0, 0),
            new Complex(1, 0),
            new Complex(2, 0)
        );

        ComplexPolynomial p2 = new ComplexPolynomial(
            new Complex(1, 0),
            new Complex(3, 0)
        );

        ComplexPolynomial mul = p1.multiply(p2);
        List<Complex> factors = mul.getFactors();

        assertEquals(4, factors.size());

        assertEquals(0, factors.get(0).getReal(), 10E-5);
        assertEquals(0, factors.get(0).getImaginary(), 10E-5);

        assertEquals(1, factors.get(1).getReal(), 10E-5);
        assertEquals(0, factors.get(1).getImaginary(), 10E-5);

        assertEquals(5, factors.get(2).getReal(), 10E-5);
        assertEquals(0, factors.get(2).getImaginary(), 10E-5);

        assertEquals(6, factors.get(3).getReal(), 10E-5);
        assertEquals(0, factors.get(3).getImaginary(), 10E-5);
    }

    @Test
    public void testDerivingAPolynomial() {
        ComplexPolynomial p = new ComplexPolynomial(
            new Complex(1, 0),
            new Complex(5, 0),
            new Complex(2, 0),
            new Complex(7, 2)
        );

        ComplexPolynomial derivative = p.derive();
        List<Complex> factors = derivative.getFactors();

        assertEquals(3, factors.size());

        assertEquals(5, factors.get(0).getReal(),  10E-5);
        assertEquals(0, factors.get(0).getImaginary(), 10E-5);

        assertEquals(4, factors.get(1).getReal(), 10E-5);
        assertEquals(0, factors.get(1).getImaginary(), 10E-5);

        assertEquals(21, factors.get(2).getReal(), 10E-5);
        assertEquals(6, factors.get(2).getImaginary(), 10E-5);
    }

    @Test
    public void testApplyingAValue() {
        // http://www.wolframalpha.com/input/?i=(0%2B1i)+%2B+(1%2B0i)*(2%2B3i)+%2B+(2%2B2i)*(2%2B3i)%5E2

        ComplexPolynomial p = new ComplexPolynomial(
            new Complex(0, 1),
            new Complex(1, 0),
            new Complex(2, 2)
        );

        Complex result = p.apply(new Complex(2, 3));

        assertEquals(-32, result.getReal(), 10E-5);
        assertEquals(18, result.getImaginary(), 10E-5);
    }
}