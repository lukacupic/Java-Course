package hr.fer.zemris.java.hw04.db;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ConditionalExpressionTest {

    @Test
    public void testTheConditionalExpressionWithLike() {
        ConditionalExpression expr = new ConditionalExpression(
            FieldValueGetters.LAST_NAME,
            "Pre*ect",
            ComparisonOperators.LIKE
        );

        StudentRecord record = new StudentRecord(
            "0000000042",
            "Prefect",
            "Ford",
            10
        );

        boolean recordSatisfies = expr.getComparisonOperator().satisfied(
            expr.getFieldGetter().get(record), // returns lastName from the given record
            expr.getStringLiteral() // returns "Bos*"
        );

        assertTrue(recordSatisfies);
    }
}