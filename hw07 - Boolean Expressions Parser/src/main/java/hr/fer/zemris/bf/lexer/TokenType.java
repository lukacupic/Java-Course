package hr.fer.zemris.bf.lexer;

/**
 * This class represents different kinds of Tokens which are supportable
 * by the class {@link Token}.
 *
 * @author Luka Čupić
 */
public enum TokenType {

    /**
     * Represents no-more-tokens.
     */
    EOF,

    /**
     * Represents a variable.
     */
    VARIABLE,

    /**
     * Represents a constant.
     */
    CONSTANT,

    /**
     * Represents an operator.
     */
    OPERATOR,

    /**
     * Represents an open bracket.
     */
    OPEN_BRACKET,

    /**
     * Represents a closed bracket.
     */
    CLOSED_BRACKET
}
