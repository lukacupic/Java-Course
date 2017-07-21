package hr.fer.zemris.java.hw04.db;

import hr.fer.zemris.java.hw04.db.lexer.QueryLexer;
import hr.fer.zemris.java.hw04.db.lexer.QueryLexerException;
import hr.fer.zemris.java.hw04.db.lexer.TokenType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class QueryLexerTest {

    @Test
    public void testLegalTokens() {
        QueryLexer lexer = new QueryLexer("and <\"hey my string\">=attr!=");
        assertEquals(lexer.nextToken().getType(), TokenType.LOGICAL_OPERATOR);
        assertEquals(lexer.nextToken().getType(), TokenType.CONDITIONAL_OPERATOR);
        assertEquals(lexer.nextToken().getType(), TokenType.STRING);
        assertEquals(lexer.nextToken().getType(), TokenType.CONDITIONAL_OPERATOR);
        assertEquals(lexer.nextToken().getType(), TokenType.ATTRIBUTE);
        assertEquals(lexer.nextToken().getType(), TokenType.CONDITIONAL_OPERATOR);
    }

    @Test(expected = QueryLexerException.class)
    public void testIllegalToken() {
        QueryLexer lexer = new QueryLexer("and \"string\" 4218");
        assertEquals(lexer.nextToken().getType(), TokenType.LOGICAL_OPERATOR);
        assertEquals(lexer.nextToken().getType(), TokenType.STRING);
        lexer.nextToken();
    }
}