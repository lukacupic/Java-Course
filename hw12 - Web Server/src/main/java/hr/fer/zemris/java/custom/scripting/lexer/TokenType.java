package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Represents a set of Token constants supportable by the class {@link Token}.
 *
 * @author Luka Čupić
 */
public enum TokenType {

    /**
     * Represents a token for text.
     */
    TEXT,

    /**
     * Represents a variable inside a tag.
     */
    VARIABLE,

    /**
     * Represents an integer inside a tag.
     */
    INTEGER,

    /**
     * Represents a double value inside a tag.
     */
    DOUBLE,

    /**
     * Represents a string inside a tag.
     */
    STRING,

    /**
     * Represents a  function inside a tag.
     */
    FUNCTION,

    /**
     * Represents an operator inside a tag.
     */
    OPERATOR,

    /**
     * Represents a tag (name) whose value cannot be represented by the
     * variable constant.
     */
    TAG_NAME,

    /**
     * Represents the beginning of a tag.
     */
    TAG_START,

    /**
     * Represents the ending of a tag.
     */
    TAG_END,

    /**
     * Represents "end-of-file".
     */
    EOF
}