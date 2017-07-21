package hr.fer.zemris.java.hw03.prob1;

/**
 * Represents a set of Token constants supportable by the class {@link Token}.
 *
 * @author Luka Čupić
 */
public enum TokenType {

    /**
     * Represents a word, defined as one or more letters.
     */
    WORD,

    /**
     * Represents a number, defined as one or more digits.
     */
    NUMBER,

    /**
     * Represents a single character of a particular meaning.
     */
    SYMBOL,

    /**
     * Represents a token for "end-of-file".
     */
    EOF
}
