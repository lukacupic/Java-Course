package hr.fer.zemris.java.custom.scripting.lexer;

import hr.fer.zemris.java.hw03.prob1.LexerException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LexerTest {

    @Test
    public void testText() {
        Lexer lexer = new Lexer("my awesome lexically correct text.");

        assertEquals(TokenType.TEXT, lexer.nextToken().getType());
    }

    @Test
    public void testStartTag() {
        Lexer lexer = new Lexer("{$");

        assertEquals(TokenType.TAG_START, lexer.nextToken().getType());
    }

    @Test
    public void testEndTag() {
        Lexer lexer = new Lexer("$}");
        lexer.setState(LexerState.TAG);

        assertEquals(TokenType.TAG_END, lexer.nextToken().getType());
    }

    @Test
    public void testLegalVariableName() {
        Lexer lexer = new Lexer("this_is_my_legal_var");
        lexer.setState(LexerState.TAG);

        assertEquals(TokenType.VARIABLE, lexer.nextToken().getType());
    }

    @Test(expected = LexerException.class)
    public void testIllegalVariableName() {
        Lexer lexer = new Lexer("my_il!egal_var2_name");
        lexer.setState(LexerState.TAG);

        lexer.nextToken();
    }

    @Test
    public void testIfLexerRecognizesString() {
        Lexer lexer = new Lexer("\"this should be just fine ... {$ 42 $}\"");
        lexer.setState(LexerState.TAG);

        assertEquals(TokenType.STRING, lexer.nextToken().getType());
    }

    @Test
    public void testIfLexerRecognizesInteger() {
        Lexer lexer = new Lexer("3.1415926535897");
        lexer.setState(LexerState.TAG);

        assertEquals(TokenType.DOUBLE, lexer.nextToken().getType());
    }

    @Test
    public void testIfLexerRecognizesFunction() {
        Lexer lexer = new Lexer("@function_2");
        lexer.setState(LexerState.TAG);

        assertEquals(TokenType.FUNCTION, lexer.nextToken().getType());
    }

    @Test(expected = LexerException.class)
    public void testIfLexerRecognizesIllegalFunction() {
        Lexer lexer = new Lexer("@32_func");
        lexer.setState(LexerState.TAG);

        lexer.nextToken();
    }

    @Test
    public void testIfLexerReturnsMultipleTokens() {
        Lexer lexer = new Lexer("this is just a normal \\{$ text with a few numbers: 1, 1, 2, 3, 5" +
            "{$=  i \"0.0000\"/-5817.31 \"my \\nsecond \\\"string... \"+ 42 @func5 my_var @math_func $}");

        assertEquals(TokenType.TEXT, lexer.nextToken().getType());
        assertEquals(TokenType.TAG_START, lexer.nextToken().getType());

        lexer.setState(LexerState.TAG);

        assertEquals(TokenType.TAG_NAME, lexer.nextToken().getType());
        assertEquals(TokenType.VARIABLE, lexer.nextToken().getType());
        assertEquals(TokenType.STRING, lexer.nextToken().getType());
        assertEquals(TokenType.OPERATOR, lexer.nextToken().getType());
        assertEquals(TokenType.DOUBLE, lexer.nextToken().getType());
        assertEquals(TokenType.STRING, lexer.nextToken().getType());
        assertEquals(TokenType.OPERATOR, lexer.nextToken().getType());
        assertEquals(TokenType.INTEGER, lexer.nextToken().getType());
        assertEquals(TokenType.FUNCTION, lexer.nextToken().getType());
        assertEquals(TokenType.VARIABLE, lexer.nextToken().getType());
        assertEquals(TokenType.FUNCTION, lexer.nextToken().getType());
        assertEquals(TokenType.TAG_END, lexer.nextToken().getType());
        assertEquals(TokenType.EOF, lexer.nextToken().getType());
    }

    @Test
    public void testAForLoopTag() {
        Lexer lexer = new Lexer("{$ FOR sco_re \"-1\"10 \"1\" $}");

        assertEquals(TokenType.TAG_START, lexer.nextToken().getType());

        lexer.setState(LexerState.TAG);

        assertEquals(TokenType.VARIABLE, lexer.nextToken().getType());
        assertEquals(TokenType.VARIABLE, lexer.nextToken().getType());
        assertEquals(TokenType.STRING, lexer.nextToken().getType());
        assertEquals(TokenType.INTEGER, lexer.nextToken().getType());
        assertEquals(TokenType.STRING, lexer.nextToken().getType());
        assertEquals(TokenType.TAG_END, lexer.nextToken().getType());
        assertEquals(TokenType.EOF, lexer.nextToken().getType());
    }

    @Test
    public void testAnEchoTag() {
        Lexer lexer = new Lexer("{$=i$}");

        assertEquals(TokenType.TAG_START, lexer.nextToken().getType());

        lexer.setState(LexerState.TAG);

        assertEquals(TokenType.TAG_NAME, lexer.nextToken().getType());
        assertEquals(TokenType.VARIABLE, lexer.nextToken().getType());
        assertEquals(TokenType.TAG_END, lexer.nextToken().getType());
        assertEquals(TokenType.EOF, lexer.nextToken().getType());
    }
}
