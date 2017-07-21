package hr.fer.zemris.java.hw02;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ComplexNumberTest {

    @Test
    public void createComplexNumberFromRealPart() {
        ComplexNumber complex = ComplexNumber.fromReal(5);

        assertEquals(5, complex.getReal(), 10E-8);
        assertEquals(0, complex.getImaginary(), 10E-8);
    }

    @Test
    public void createComplexNumberFromMagnitudeAndAngle() {
        ComplexNumber complex = ComplexNumber.fromMagnitudeAndAngle(1.414213, 0.785398);

        assertEquals(1, complex.getReal(), 10E-5);
        assertEquals(1, complex.getImaginary(), 10E-5);
    }

    @Test
    public void createComplexNumberFromImaginaryPart() {
        ComplexNumber complex = ComplexNumber.fromImaginary(-6);

        assertEquals(0, complex.getReal(), 10E-8);
        assertEquals(-6, complex.getImaginary(), 10E-8);
    }

    @Test
    public void parseABunchOfComplexNumbers() {
        ComplexNumber complex;

        complex = ComplexNumber.parse("2");
        assertEquals(2, complex.getReal(), 10E-8);
        assertEquals(0, complex.getImaginary(), 10E-8);

        complex = ComplexNumber.parse("-6");
        assertEquals(-6, complex.getReal(), 10E-8);
        assertEquals(0, complex.getImaginary(), 10E-8);

        complex = ComplexNumber.parse("i");
        assertEquals(0, complex.getReal(), 10E-8);
        assertEquals(1, complex.getImaginary(), 10E-8);

        complex = ComplexNumber.parse("-i");
        assertEquals(0, complex.getReal(), 10E-8);
        assertEquals(-1, complex.getImaginary(), 10E-8);

        complex = ComplexNumber.parse("42i");
        assertEquals(0, complex.getReal(), 10E-8);
        assertEquals(42, complex.getImaginary(), 10E-8);

        complex = ComplexNumber.parse("-7i");
        assertEquals(0, complex.getReal(), 10E-8);
        assertEquals(-7, complex.getImaginary(), 10E-8);

        complex = ComplexNumber.parse("16+i");
        assertEquals(16, complex.getReal(), 10E-8);
        assertEquals(1, complex.getImaginary(), 10E-8);

        complex = ComplexNumber.parse("-314+75i");
        assertEquals(-314, complex.getReal(), 10E-8);
        assertEquals(75, complex.getImaginary(), 10E-8);

        complex = ComplexNumber.parse("-17+4i");
        assertEquals(-17, complex.getReal(), 10E-8);
        assertEquals(4, complex.getImaginary(), 10E-8);

        complex = ComplexNumber.parse("-9-27i");
        assertEquals(-9, complex.getReal(), 10E-8);
        assertEquals(-27, complex.getImaginary(), 10E-8);

        complex = ComplexNumber.parse("-1-i");
        assertEquals(-1, complex.getReal(), 10E-8);
        assertEquals(-1, complex.getImaginary(), 10E-8);
    }

    @Test
    public void parseSomethingThatIsNotAComplexNumber() {
        boolean exceptionHappened = false;

        try {
            ComplexNumber.parse("štefica");
        } catch (IllegalArgumentException ex) {
            exceptionHappened = true;
        }

        assertTrue(exceptionHappened);
    }

    @Test
    public void getRealPartOfComplexNumber() {
        ComplexNumber current = new ComplexNumber(42, -13);

        assertEquals(42, current.getReal(), 10E-8);
    }

    @Test
    public void getImaginaryPartOfComplexNumber() {
        ComplexNumber current = new ComplexNumber(42, -13);

        assertEquals(-13, current.getImaginary(), 10E-8);
    }

    @Test
    public void getMagitudeOfComplexNumber() {
        ComplexNumber current = new ComplexNumber(-1, -1);

        assertEquals(1.414213, current.getMagnitude(), 10E-5);
    }

    @Test
    public void getAngleOfComplexNumber() {
        ComplexNumber current = new ComplexNumber(1, 1);
        assertEquals(0.785398, current.getAngle(), 10E-5);

        current = new ComplexNumber(-1, -1);
        assertEquals(3.926991, current.getAngle(), 10E-5);
    }

    @Test
    public void addTwoComplexNumbers() {
        ComplexNumber current = new ComplexNumber(5, 5);
        ComplexNumber other = new ComplexNumber(-5, -5);

        ComplexNumber result = current.add(other);

        assertEquals(0, result.getReal(), 10E-8);
        assertEquals(0, result.getImaginary(), 10E-8);
    }

    @Test
    public void subtractTwoComplexNumbers() {
        ComplexNumber current = new ComplexNumber(5, 5);
        ComplexNumber other = new ComplexNumber(5, 5);

        ComplexNumber result = current.sub(other);

        assertEquals(0, result.getReal(), 10E-8);
        assertEquals(0, result.getImaginary(), 10E-8);
    }

    @Test
    public void multiplyTwoComplexNumbers() {
        ComplexNumber current = new ComplexNumber(13, 42);
        ComplexNumber other = new ComplexNumber(1, 0);

        ComplexNumber result = current.mul(other);

        assertEquals(13, result.getReal(), 10E-8);
        assertEquals(42, result.getImaginary(), 10E-8);
    }

    @Test
    public void divideTwoComplexNumbers() {
        ComplexNumber current = new ComplexNumber(3, -2);
        ComplexNumber other = new ComplexNumber(-3, 2);

        ComplexNumber result = current.div(other);

        assertEquals(-1, result.getReal(), 10E-8);
        assertEquals(0, result.getImaginary(), 10E-8);
    }

    @Test
    public void divideComplexNumberByZero() {
        ComplexNumber current = new ComplexNumber(3, -2);
        ComplexNumber other = new ComplexNumber(0, 0);

        boolean exceptionHappened = false;

        try {
            current.div(other);
        } catch (IllegalArgumentException ex) {
            exceptionHappened = true;
        }

        assertTrue(exceptionHappened);
    }

    @Test
    public void raiseComplexNumbersToSecondPower() {
        ComplexNumber current = new ComplexNumber(2, 3);

        ComplexNumber result = current.power(2);

        assertEquals(-5, result.getReal(), 10E-8);
        assertEquals(12, result.getImaginary(), 10E-8);
    }

    @Test
    public void raiseComplexNumbersToNegativePower() {
        ComplexNumber current = new ComplexNumber(2, 3);

        boolean exceptionHappened = false;

        try {
            current.power(-7);
        } catch (IllegalArgumentException ex) {
            exceptionHappened = true;
        }

        assertTrue(exceptionHappened);
    }

    @Test
    public void findFifthRootsOfComplexNumber() {
        ComplexNumber current = new ComplexNumber(-5, 12);

        ComplexNumber[] roots = current.root(2);

        assertEquals(2, roots[0].getReal(), 10E-8);
        assertEquals(3, roots[0].getImaginary(), 10E-8);

        assertEquals(-2, roots[1].getReal(), 10E-8);
        assertEquals(-3, roots[1].getImaginary(), 10E-8);
    }

    @Test
    public void findNegativeRootsOfComplexNumber() {
        ComplexNumber current = new ComplexNumber(-5, 12);

        boolean exceptionHappened = false;

        try {
            current.root(-2);
        } catch (IllegalArgumentException ex) {
            exceptionHappened = true;
        }

        assertTrue(exceptionHappened);
    }
}
