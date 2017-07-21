package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Represents the current state of the {@link Lexer}.
 *
 * @author Luka Čupić
 */
public enum LexerState {

    /**
     * State for processing text.
     */
    TEXT,

    /**
     * State for processing tags.
     */
    TAG
}
