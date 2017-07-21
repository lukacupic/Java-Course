package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Represents the current state of a {@link hr.fer.zemris.java.hw03.prob1.Lexer}.
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
