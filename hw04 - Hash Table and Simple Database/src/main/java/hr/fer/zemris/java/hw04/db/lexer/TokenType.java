package hr.fer.zemris.java.hw04.db.lexer;

/**
 * Represents a set of Token constants supportable by the class {@link Token}.
 *
 * @author Luka Čupić
 */
public enum TokenType {

    /**
     * Represents an attribute, defined as a sequence of letters.
     */
    ATTRIBUTE,

    /**
     * Represents a conditional operator.
     */
    CONDITIONAL_OPERATOR,

    /**
     * Represents a logical operator.
     */
    LOGICAL_OPERATOR,

    /**
     * Represents a string literal.
     */
    STRING,

    /**
     * Represents the end of file.
     */
    EOF
}