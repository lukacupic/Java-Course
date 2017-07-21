package hr.fer.zemris.bf.lexer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LexerTest {

    @Test
    public void testNegatingAVariable() {
        Lexer lexer = new Lexer("Not a");
        Token token;

        token = lexer.nextToken();
        assertEquals(TokenType.OPERATOR, token.getTokenType());
        assertEquals("not", token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.VARIABLE, token.getTokenType());
        assertEquals("A", token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.EOF, token.getTokenType());
        assertEquals(null, token.getTokenValue());
    }

    @Test
    public void testSingleOperator() {
        Lexer lexer = new Lexer("+");
        Token token;

        token = lexer.nextToken();
        assertEquals(TokenType.OPERATOR, token.getTokenType());
        assertEquals("or", token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.EOF, token.getTokenType());
        assertEquals(null, token.getTokenValue());
    }

    @Test
    public void testSingleConstant() {
        Lexer lexer = new Lexer("0");
        Token token;

        token = lexer.nextToken();
        assertEquals(TokenType.CONSTANT, token.getTokenType());
        assertEquals(false, token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.EOF, token.getTokenType());
        assertEquals(null, token.getTokenValue());
    }

    @Test
    public void testSingleConstantButWrittenAsString() {
        Lexer lexer = new Lexer("tRue");
        Token token;

        token = lexer.nextToken();
        assertEquals(TokenType.CONSTANT, token.getTokenType());
        assertEquals(true, token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.EOF, token.getTokenType());
        assertEquals(null, token.getTokenValue());
    }

    @Test
    public void testExpressionWithOneAndOperation() {
        Lexer lexer = new Lexer("A aNd b");
        Token token;

        token = lexer.nextToken();
        assertEquals(TokenType.VARIABLE, token.getTokenType());
        assertEquals("A", token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.OPERATOR, token.getTokenType());
        assertEquals("and", token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.VARIABLE, token.getTokenType());
        assertEquals("B", token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.EOF, token.getTokenType());
        assertEquals(null, token.getTokenValue());
    }

    @Test
    public void testExpressionWithOneOrOperation() {
        Lexer lexer = new Lexer("a or b");
        Token token;

        token = lexer.nextToken();
        assertEquals(TokenType.VARIABLE, token.getTokenType());
        assertEquals("A", token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.OPERATOR, token.getTokenType());
        assertEquals("or", token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.VARIABLE, token.getTokenType());
        assertEquals("B", token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.EOF, token.getTokenType());
        assertEquals(null, token.getTokenValue());
    }

    @Test
    public void testExpressionWithOneXorOperation() {
        Lexer lexer = new Lexer("a xoR B");
        Token token;

        token = lexer.nextToken();
        assertEquals(TokenType.VARIABLE, token.getTokenType());
        assertEquals("A", token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.OPERATOR, token.getTokenType());
        assertEquals("xor", token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.VARIABLE, token.getTokenType());
        assertEquals("B", token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.EOF, token.getTokenType());
        assertEquals(null, token.getTokenValue());
    }

    @Test
    public void testExpressionWithTwoAndOperation() {
        Lexer lexer = new Lexer("A and b * c");
        Token token;

        token = lexer.nextToken();
        assertEquals(TokenType.VARIABLE, token.getTokenType());
        assertEquals("A", token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.OPERATOR, token.getTokenType());
        assertEquals("and", token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.VARIABLE, token.getTokenType());
        assertEquals("B", token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.OPERATOR, token.getTokenType());
        assertEquals("and", token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.VARIABLE, token.getTokenType());
        assertEquals("C", token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.EOF, token.getTokenType());
        assertEquals(null, token.getTokenValue());
    }

    @Test
    public void testExpressionWithTwoOrOperations() {
        Lexer lexer = new Lexer("A or bla_bla_bla or c");
        Token token;

        token = lexer.nextToken();
        assertEquals(TokenType.VARIABLE, token.getTokenType());
        assertEquals("A", token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.OPERATOR, token.getTokenType());
        assertEquals("or", token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.VARIABLE, token.getTokenType());
        assertEquals("BLA_BLA_BLA", token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.OPERATOR, token.getTokenType());
        assertEquals("or", token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.VARIABLE, token.getTokenType());
        assertEquals("C", token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.EOF, token.getTokenType());
        assertEquals(null, token.getTokenValue());
    }

    @Test
    public void testExpressionWithTwoXorOperations() {
        Lexer lexer = new Lexer("a xor b :+: c");
        Token token;

        token = lexer.nextToken();
        assertEquals(TokenType.VARIABLE, token.getTokenType());
        assertEquals("A", token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.OPERATOR, token.getTokenType());
        assertEquals("xor", token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.VARIABLE, token.getTokenType());
        assertEquals("B", token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.OPERATOR, token.getTokenType());
        assertEquals("xor", token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.VARIABLE, token.getTokenType());
        assertEquals("C", token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.EOF, token.getTokenType());
        assertEquals(null, token.getTokenValue());
    }

    @Test
    public void testDoubleNegative() {
        Lexer lexer = new Lexer("not not a");
        Token token;

        token = lexer.nextToken();
        assertEquals(TokenType.OPERATOR, token.getTokenType());
        assertEquals("not", token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.OPERATOR, token.getTokenType());
        assertEquals("not", token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.VARIABLE, token.getTokenType());
        assertEquals("A", token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.EOF, token.getTokenType());
        assertEquals(null, token.getTokenValue());
    }

    @Test
    public void testExpressionWithMultipleOperations() {
        Lexer lexer = new Lexer("a or b xor c and d");
        Token token;

        token = lexer.nextToken();
        assertEquals(TokenType.VARIABLE, token.getTokenType());
        assertEquals("A", token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.OPERATOR, token.getTokenType());
        assertEquals("or", token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.VARIABLE, token.getTokenType());
        assertEquals("B", token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.OPERATOR, token.getTokenType());
        assertEquals("xor", token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.VARIABLE, token.getTokenType());
        assertEquals("C", token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.OPERATOR, token.getTokenType());
        assertEquals("and", token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.VARIABLE, token.getTokenType());
        assertEquals("D", token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.EOF, token.getTokenType());
        assertEquals(null, token.getTokenValue());
    }

    @Test
    public void testExpressionWithMultipleBrackets() {
        Lexer lexer = new Lexer("(a + b) xor (c or d)");
        Token token;

        token = lexer.nextToken();
        assertEquals(TokenType.OPEN_BRACKET, token.getTokenType());
        assertEquals('(', token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.VARIABLE, token.getTokenType());
        assertEquals("A", token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.OPERATOR, token.getTokenType());
        assertEquals("or", token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.VARIABLE, token.getTokenType());
        assertEquals("B", token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.CLOSED_BRACKET, token.getTokenType());
        assertEquals(')', token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.OPERATOR, token.getTokenType());
        assertEquals("xor", token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.OPEN_BRACKET, token.getTokenType());
        assertEquals('(', token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.VARIABLE, token.getTokenType());
        assertEquals("C", token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.OPERATOR, token.getTokenType());
        assertEquals("or", token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.VARIABLE, token.getTokenType());
        assertEquals("D", token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.CLOSED_BRACKET, token.getTokenType());
        assertEquals(')', token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.EOF, token.getTokenType());
        assertEquals(null, token.getTokenValue());
    }

    @Test(expected = LexerException.class)
    public void testIllegalConstantValue() {
        Lexer lexer = new Lexer("a and 10");
        Token token;

        token = lexer.nextToken();
        assertEquals(TokenType.VARIABLE, token.getTokenType());
        assertEquals("A", token.getTokenValue());

        token = lexer.nextToken();
        assertEquals(TokenType.OPERATOR, token.getTokenType());
        assertEquals("and", token.getTokenValue());

        lexer.nextToken(); // throws!
    }

    @Test
    public void testReturningAlreadyGeneratedToken() {
        Lexer lexer = new Lexer("a");
        Token token;

        token = lexer.nextToken();
        assertEquals(TokenType.VARIABLE, token.getTokenType());
        assertEquals("A", token.getTokenValue());

        assertEquals(TokenType.VARIABLE, lexer.getToken().getTokenType());
        assertEquals("A", lexer.getToken().getTokenValue());
    }

    @Test
    public void testIfGetTokenGeneratesTheFirstToken() {
        Lexer lexer = new Lexer("a");

        assertEquals(TokenType.VARIABLE, lexer.getToken().getTokenType());
        assertEquals("A", lexer.getToken().getTokenValue());
    }
}