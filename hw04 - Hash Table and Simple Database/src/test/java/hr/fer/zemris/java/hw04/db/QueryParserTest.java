package hr.fer.zemris.java.hw04.db;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class QueryParserTest {

    @Test
    public void testSingleExpression() {
        String input = "jmbag != \"9000\"";

        QueryParser parser = new QueryParser(input);

        ConditionalExpression expr1 = parser.getQuery().get(0);

        assertEquals(expr1.getFieldGetter(), FieldValueGetters.JMBAG);
        assertEquals(expr1.getComparisonOperator(), ComparisonOperators.NOT_EQUAL);
        assertEquals(expr1.getStringLiteral(), "9000");
    }

    @Test
    public void testMultipleExpressions() {
        String input = "lastName = \"Bond\" AND firstName = \"James\" AND lastName = \"Bond\"";

        QueryParser parser = new QueryParser(input);

        ConditionalExpression expr1 = parser.getQuery().get(0);
        ConditionalExpression expr2 = parser.getQuery().get(1);
        ConditionalExpression expr3 = parser.getQuery().get(2);

        assertEquals(expr1.getFieldGetter(), FieldValueGetters.LAST_NAME);
        assertEquals(expr1.getComparisonOperator(), ComparisonOperators.EQUALS);
        assertEquals(expr1.getStringLiteral(), "Bond");

        assertEquals(expr2.getFieldGetter(), FieldValueGetters.FIRST_NAME);
        assertEquals(expr2.getComparisonOperator(), ComparisonOperators.EQUALS);
        assertEquals(expr2.getStringLiteral(), "James");

        assertEquals(expr3.getFieldGetter(), FieldValueGetters.LAST_NAME);
        assertEquals(expr3.getComparisonOperator(), ComparisonOperators.EQUALS);
        assertEquals(expr3.getStringLiteral(), "Bond");
    }

    @Test(expected = QueryParserException.class)
    public void testIllegalAttributeName() {
        String input = "illegalAttributeName = \"something\"";

        QueryParser parser = new QueryParser(input);
    }

    @Test(expected = QueryParserException.class)
    public void testIllegalOperatorName() {
        String input = "illegalAttributeName ? \"something\"";

        QueryParser parser = new QueryParser(input);
    }

    @Test(expected = QueryParserException.class)
    public void testIncompleteCommand() {
        String input = "lastName = \"Skywalker\" AND";

        QueryParser parser = new QueryParser(input);
    }

    @Test(expected = QueryParserException.class)
    public void testIllegalCommand() {
        String input = "lastName = firstName";

        QueryParser parser = new QueryParser(input);
    }
}