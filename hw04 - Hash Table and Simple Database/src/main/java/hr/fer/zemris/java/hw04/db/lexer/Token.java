package hr.fer.zemris.java.hw04.db.lexer;

/**
 * Represents a lexical unit which groups one or more characters from the input sequence of characters.
 * Acceptable token types are as defined in the class {@link TokenType}.
 *
 * @author Luka Čupić
 */
public class Token {

    /**
     * The type of this token.
     */
    private final TokenType type;

    /**
     * The value of this token.
     */
    private final Object value;

    /**
     * Creates a new Token of type {@code type} and of value {@code value}.
     *
     * @param type  type of this token.
     * @param value value of this token.
     */
    public Token(TokenType type, Object value) {
        if (type == null) {
            throw new IllegalArgumentException("Token cannot be of type null.");
        }

        this.type = type;
        this.value = value;
    }

    /**
     * Returns the value of this token.
     *
     * @return the value of this token.
     */
    public Object getValue() {
        return value;
    }

    /**
     * Returns the type of this token.
     *
     * @return the type of this token.
     */
    public TokenType getType() {
        return type;
    }

}
