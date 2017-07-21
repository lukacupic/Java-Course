package hr.fer.zemris.bf.utils;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertTrue;

public class UtilTest {

    @Test
    public void convertNumberThreeToBinaryByteArrayOfLengthTwo() {
        assertTrue((Arrays.equals(new byte[]{1, 1}, Util.indexToByteArray(3, 2))));
    }

    @Test
    public void convertNumberThreeToBinaryByteArrayOfLengthFour() {
        assertTrue((Arrays.equals(new byte[]{0, 0, 1, 1}, Util.indexToByteArray(3, 4))));
    }

    @Test
    public void convertNumberThreeToBinaryByteArrayOfLengthSix() {
        assertTrue((Arrays.equals(new byte[]{0, 0, 0, 0, 1, 1}, Util.indexToByteArray(3, 6))));
    }

    @Test
    public void convertNegativeNumberToBinaryByteArrayOfLengthThirtyTwo() {
        byte[] binary = new byte[]{
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0
        };
        assertTrue((Arrays.equals(binary, Util.indexToByteArray(-2, 32))));
    }

    @Test
    public void convertNegativeNumberToBinaryByteArrayOfLengthSixteen() {
        byte[] binary = new byte[]{
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0
        };
        assertTrue((Arrays.equals(binary, Util.indexToByteArray(-2, 16))));
    }

    @Test
    public void convertNumberOfLengthTooShortForReconstruction() {
        assertTrue((Arrays.equals(new byte[]{0, 0, 1, 1}, Util.indexToByteArray(19, 4))));
    }
}