package hr.fer.zemris.math;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ComplexTest {

    @Test
    public void testCalculatingModuleOfAComplexNumber() {
        Complex current = new Complex(3, 4);

        assertEquals(5, current.module(), 10E-7);
    }

    @Test
    public void testAddingComplexNumbers() {
        Complex current = new Complex(3, 4);
        Complex other = new Complex(5, -6);

        Complex result = current.add(other);

        assertEquals(8, result.getReal(), 10E-7);
        assertEquals(-2, result.getImaginary(), 10E-7);
    }

    @Test
    public void testSubtractingComplexNumbers() {
        Complex current = new Complex(3, 4);
        Complex other = new Complex(5, -6);

        Complex result = current.sub(other);

        assertEquals(-2, result.getReal(), 10E-7);
        assertEquals(10, result.getImaginary(), 10E-7);
    }

    @Test
    public void testMultiplyingComplexNumbers() {
        Complex c1 = new Complex(3, 4);
        Complex c2 = new Complex(5, -6);

        Complex result = c1.multiply(c2);

        assertEquals(39, result.getReal(), 10E-7);
        assertEquals(2, result.getImaginary(), 10E-7);
    }

    @Test
    public void testdividingComplexNumbers() {
        Complex current = new Complex(3, 4);
        Complex other = new Complex(5, -6);

        Complex result = current.divide(other);

        assertEquals(-0.14754, result.getReal(), 10E-5);
        assertEquals(0.62295, result.getImaginary(), 10E-5);
    }

    @Test
    public void testNegatingComplexNumber() {
        Complex current = new Complex(3, 4);
        Complex negated = current.negate();

        assertEquals(-3, negated.getReal(), 10E-7);
        assertEquals(-4, negated.getImaginary(), 10E-7);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDividingComplexNumberByZero() {
        Complex c1 = new Complex(3, 4);
        Complex c2 = new Complex(0, 0);

        c1.divide(c2);
    }

    @Test
    public void testRaisingComplexNumberToSecondPower() {
        Complex c1 = new Complex(3, 4);

        Complex result = c1.power(2);

        assertEquals(-7, result.getReal(), 10E-7);
        assertEquals(24, result.getImaginary(), 10E-7);
    }

    @Test(expected = IllegalArgumentException.class)
    public void raiseComplexNumberToNegativePower() {
        Complex current = new Complex(3, 4);
        current.power(-42);
    }

    @Test
    public void testCalculatingSecondRootsOfTheComplexNumber() {
        Complex current = new Complex(3, 4);

        List<Complex> roots = current.root(2);

        assertEquals(2, roots.size());

        assertEquals(2, roots.get(0).getReal(), 10E-7);
        assertEquals(1, roots.get(0).getImaginary(), 10E-7);

        assertEquals(-2, roots.get(1).getReal(), 10E-7);
        assertEquals(-1, roots.get(1).getImaginary(), 10E-7);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalculatingNegativeRoot() {
        Complex current = new Complex(3, 4);
        current.root(-42);
    }

    @Test
    public void testCalculatingAngleOfTheComplexNumber() {
        Complex current = new Complex(3, 4);
        assertEquals(0.92729, current.angle(), 10E-5);
    }

    @Test
    public void testCalcualatingRealPartOfTheComplexNumber() {
        Complex current = new Complex(3, 4);

        assertEquals(3, current.getReal(), 10E-7);
    }

    @Test
    public void testCalcualatingImaginaryPartOfTheComplexNumber() {
        Complex current = new Complex(3, 4);

        assertEquals(4, current.getImaginary(), 10E-7);
    }

    @Test
    public void testParsingComplexNumbers() {
        Complex complex;

        complex = Complex.parse("2");
        assertEquals(2, complex.getReal(), 10E-8);
        assertEquals(0, complex.getImaginary(), 10E-8);

        complex = Complex.parse("-6");
        assertEquals(-6, complex.getReal(), 10E-8);
        assertEquals(0, complex.getImaginary(), 10E-8);

        complex = Complex.parse("i");
        assertEquals(0, complex.getReal(), 10E-8);
        assertEquals(1, complex.getImaginary(), 10E-8);

        complex = Complex.parse("-i");
        assertEquals(0, complex.getReal(), 10E-8);
        assertEquals(-1, complex.getImaginary(), 10E-8);

        complex = Complex.parse("i42");
        assertEquals(0, complex.getReal(), 10E-8);
        assertEquals(42, complex.getImaginary(), 10E-8);

        complex = Complex.parse("-i7");
        assertEquals(0, complex.getReal(), 10E-8);
        assertEquals(-7, complex.getImaginary(), 10E-8);

        complex = Complex.parse("16 + i");
        assertEquals(16, complex.getReal(), 10E-8);
        assertEquals(1, complex.getImaginary(), 10E-8);

        complex = Complex.parse("- 314 + i75");
        assertEquals(-314, complex.getReal(), 10E-8);
        assertEquals(75, complex.getImaginary(), 10E-8);

        complex = Complex.parse("-17 + i4");
        assertEquals(-17, complex.getReal(), 10E-8);
        assertEquals(4, complex.getImaginary(), 10E-8);

        complex = Complex.parse("-9-i27");
        assertEquals(-9, complex.getReal(), 10E-8);
        assertEquals(-27, complex.getImaginary(), 10E-8);

        complex = Complex.parse("-1-i");
        assertEquals(-1, complex.getReal(), 10E-8);
        assertEquals(-1, complex.getImaginary(), 10E-8);

        complex = Complex.parse("0");
        assertEquals(0, complex.getReal(), 10E-8);
        assertEquals(0, complex.getImaginary(), 10E-8);

        complex = Complex.parse("i0");
        assertEquals(0, complex.getReal(), 10E-8);
        assertEquals(0, complex.getImaginary(), 10E-8);

        complex = Complex.parse("0 + i0");
        assertEquals(0, complex.getReal(), 10E-8);
        assertEquals(0, complex.getImaginary(), 10E-8);

        complex = Complex.parse("0-i0");
        assertEquals(0, complex.getReal(), 10E-8);
        assertEquals(0, complex.getImaginary(), 10E-8);
    }
}