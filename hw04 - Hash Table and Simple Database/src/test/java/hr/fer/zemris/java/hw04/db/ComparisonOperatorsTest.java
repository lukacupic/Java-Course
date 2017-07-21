package hr.fer.zemris.java.hw04.db;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ComparisonOperatorsTest {

    @Test
    public void testLessOperator() {
        IComparisonOperator op = ComparisonOperators.LESS;

        assertTrue(op.satisfied("Anica", "Štefica"));

        assertFalse(op.satisfied("Zvonimir", "Zvonimir"));
    }

    @Test
    public void testLessOrEqualOperator() {
        IComparisonOperator op = ComparisonOperators.LESS_OR_EQUAL;

        assertTrue(op.satisfied("Anica", "Štefica"));
        assertTrue(op.satisfied("Zvonimir", "Zvonimir"));

        assertFalse(op.satisfied("Zvonko", "Ana"));
    }

    @Test
    public void testGreaterOperator() {
        IComparisonOperator op = ComparisonOperators.GREATER;

        assertTrue(op.satisfied("Štefica", "Anica"));

        assertFalse(op.satisfied("Ante", "Zvonimir"));
    }

    @Test
    public void testGreaterOrEqualOperator() {
        IComparisonOperator op = ComparisonOperators.GREATER_OR_EQUAL;

        assertTrue(op.satisfied("Štefica", "Anica"));
        assertTrue(op.satisfied("Zvonimir", "Zvonimir"));

        assertFalse(op.satisfied("Andrija", "Boris"));
    }

    @Test
    public void testEqualsOperator() {
        IComparisonOperator op = ComparisonOperators.EQUALS;

        assertTrue(op.satisfied("Štefica", "Štefica"));

        assertFalse(op.satisfied("Štefica", "Brankica"));
    }

    @Test
    public void testNotEqualOperator() {
        IComparisonOperator op = ComparisonOperators.NOT_EQUAL;

        assertTrue(op.satisfied("Štefica", "Brankica"));

        assertFalse(op.satisfied("Štefica", "Štefica"));
    }

    @Test
    public void testLikeOperatorForOneCharacter() {
        IComparisonOperator op = ComparisonOperators.LIKE;

        assertTrue(op.satisfied("Zagreb", "Z*greb"));
        assertTrue(op.satisfied("Zegreb", "Z*greb"));
        assertTrue(op.satisfied("Zvgreb", "Z*greb"));

        assertFalse(op.satisfied("Z", "Z*greb"));
        assertFalse(op.satisfied("Zagreb", "Zg"));
    }

    @Test
    public void testLikeOperatorForMultipleCharacters() {
        IComparisonOperator op = ComparisonOperators.LIKE;

        assertTrue(op.satisfied("Zagreb", "Z*"));
        assertTrue(op.satisfied("Zagreb", "Zag*"));
        assertTrue(op.satisfied("Zagreb", "*agreb"));
        assertTrue(op.satisfied("Zagreb", "*eb"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLikeOperatorWithMultipleWildcards() {
        IComparisonOperator op = ComparisonOperators.LIKE;
        op.satisfied("Zagreb", "Z*g*eb");
    }
}