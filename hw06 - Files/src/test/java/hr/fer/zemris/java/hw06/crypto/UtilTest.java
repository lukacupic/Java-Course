package hr.fer.zemris.java.hw06.crypto;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class UtilTest {

    // HEXTOBYTE TESTS

    @Test
    public void convertHexToBytes() {
        byte[] bytes = Util.hextobyte("01aE22");

        assertEquals(1, bytes[0]);
        assertEquals(-82, bytes[1]);
        assertEquals(34, bytes[2]);
    }

    @Test
    public void convertUppercaseHexToBytes() {
        byte[] bytes = Util.hextobyte("01AE22");

        assertEquals(1, bytes[0]);
        assertEquals(-82, bytes[1]);
        assertEquals(34, bytes[2]);
    }

    @Test
    public void convertEmptyString() {
        byte[] bytes = Util.hextobyte("");

        assertEquals(0, bytes.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalString() {
        Util.hextobyte("01aE2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalString2() {
        Util.hextobyte("?1aE22");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalString3() {
        Util.hextobyte("štefica");
    }

    // BYTETOHEX TESTS

    @Test
    public void convertBytesToHex() {
        String hex = Util.bytetohex(new byte[]{1, -82, 34});

        assertEquals("01ae22", hex);
    }

    @Test
    public void convertEmptyByteArrayToHex() {
        String hex = Util.bytetohex(new byte[]{});

        assertEquals("", hex);
    }

}