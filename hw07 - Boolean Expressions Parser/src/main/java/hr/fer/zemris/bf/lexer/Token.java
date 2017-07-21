package hr.fer.zemris.bf.lexer;

/**
 * Represents a lexical unit which groups one or more characters from the
 * input sequence of characters. Supportable token types are as defined in
 * the class {@link TokenType}.
 *
 * @author Luka Čupić
 */
public class Token {

    /**
     * The type of this token.
     */
    private final TokenType tokenType;

    /**
     * The value of this token.
     */
    private final Object tokenValue;

    /**
     * Creates a new Token of type {@code type} and of value {@code value}.
     *
     * @param tokenType  type of this token.
     * @param tokenValue value of this token.
     */
    public Token(TokenType tokenType, Object tokenValue) {
        if (tokenType == null) {
            throw new IllegalArgumentException("Token cannot be of type null.");
        }

        this.tokenType = tokenType;
        this.tokenValue = tokenValue;
    }

    /**
     * Gets the value of this token.
     *
     * @return the value of this token.
     */
    public Object getTokenValue() {
        return tokenValue;
    }

    /**
     * Gets the type of this token.
     *
     * @return the type of this token.
     */
    public TokenType getTokenType() {
        return tokenType;
    }

    @Override
    public String toString() {
        return tokenType + ": " + tokenValue;
    }
}
