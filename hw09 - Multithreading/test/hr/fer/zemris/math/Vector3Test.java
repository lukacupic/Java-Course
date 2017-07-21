package hr.fer.zemris.math;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Vector3Test {

    /**
     * The "main" vector which will be used for most test cases.
     */
    private static Vector3 vector;

    @BeforeClass
    public static void setUp() {
        vector = new Vector3(1, 2, 3);
    }

    @Test
    public void testGettingVectorNorm() {
        assertEquals(3.741657, vector.norm(), 10E-6);
    }

    @Test
    public void testNormalizingVector() {
        Vector3 normalized = vector.normalized();

        assertEquals(0.267261, normalized.getX(), 10E-6);
        assertEquals(0.534522, normalized.getY(), 10E-6);
        assertEquals(0.801783, normalized.getZ(), 10E-6);
    }

    @Test
    public void testNormalizingVectorOfLengthZero() {
        Vector3 normalized = new Vector3(0, 0, 0).normalized();

        assertEquals(0, normalized.getX(), 10E-6);
        assertEquals(0, normalized.getY(), 10E-6);
        assertEquals(0, normalized.getZ(), 10E-6);
    }

    @Test
    public void testAddingVectors() {
        Vector3 other = new Vector3(3, 2, 1);
        Vector3 result = vector.add(other);

        assertEquals(4, result.getX(), 10E-6);
        assertEquals(4, result.getY(), 10E-6);
        assertEquals(4, result.getZ(), 10E-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddMethodWithNull() {
        vector.add(null);
    }

    @Test
    public void testSubtractingVectors() {
        Vector3 other = new Vector3(3, 2, 1);
        Vector3 result = vector.sub(other);

        assertEquals(-2, result.getX(), 10E-6);
        assertEquals(0, result.getY(), 10E-6);
        assertEquals(2, result.getZ(), 10E-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubMethodWithNull() {
        vector.add(null);
    }

    @Test
    public void testDotProductOfVectors() {
        Vector3 other = new Vector3(3, 2, 1);
        assertEquals(10, vector.dot(other), 10E-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDotMethodWithNull() {
        vector.add(null);
    }

    @Test
    public void testCrossProductOfVectors() {
        Vector3 other = new Vector3(3, 2, 1);
        Vector3 result = vector.cross(other);

        assertEquals(-4, result.getX(), 10E-6);
        assertEquals(8, result.getY(), 10E-6);
        assertEquals(-4, result.getZ(), 10E-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCrossMethodWithNull() {
        vector.add(null);
    }

    @Test
    public void testScalingAVector() {
        Vector3 scaled = vector.scale(42);

        assertEquals(42, scaled.getX(), 10E-6);
        assertEquals(84, scaled.getY(), 10E-6);
        assertEquals(126, scaled.getZ(), 10E-6);
    }

    @Test
    public void testScalingAVectorWithNegativeFactor() {
        Vector3 scaled = vector.scale(-1);

        assertEquals(-1, scaled.getX(), 10E-6);
        assertEquals(-2, scaled.getY(), 10E-6);
        assertEquals(-3, scaled.getZ(), 10E-6);
    }

    @Test
    public void testCosAngleBetweenVectors() {
        Vector3 v1 = new Vector3(56, 0, 0);
        Vector3 v2 = new Vector3(0, 42, 0);

        assertEquals(0, v1.cosAngle(v2), 10E-6);
    }

    @Test
    public void testClassGetters() {
        assertEquals(1, vector.getX(), 10E-6);
        assertEquals(2, vector.getY(), 10E-6);
        assertEquals(3, vector.getZ(), 10E-6);
    }

    @Test
    public void testToArray() {
        assertTrue(Arrays.equals(new double[]{1, 2, 3}, vector.toArray()));
    }
}
