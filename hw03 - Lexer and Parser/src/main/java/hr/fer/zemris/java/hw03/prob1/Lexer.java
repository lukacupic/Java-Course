package hr.fer.zemris.java.hw03.prob1;

/**
 * Performs a lexical analysis on a sequence of characters and converts it into a sequence of tokens.
 * Acceptable tokens are as defined in the class {@link TokenType}.
 *
 * @author Luka Čupić
 */
public class Lexer {

    /**
     * The sequence of input characters.
     */
    private char[] data;

    /**
     * The current token.
     */
    private Token token;

    /**
     * Index of the first non-processed character.
     */
    private int currentIndex;

    /**
     * Current state of the lexer.
     */
    private LexerState state;

    /**
     * The value of a token which, when read from the input, toggles the state of the lexer.
     */
    private static final char TOGGLE_SYMBOL = '#';

    /**
     * Represents empty characters.
     */
    private static final String EMPTY_CHARS = "\r\n\t ";

    /**
     * Creates a new Lexer object and stores the input text which
     * will be used as the sequence of characters to analyze.
     *
     * @param text the sequence of characters to convert to tokens.
     */
    public Lexer(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Input cannot be null.");
        }

        data = text.toCharArray();
        state = LexerState.BASIC;
    }

    /**
     * Processes and returns the next available token.
     *
     * @return the next available token.
     * @throws LexerException if an error occurred while generating the next token.
     */
    public Token nextToken() throws LexerException {
        // if there are no more tokens
        if (token != null && token.getType() == TokenType.EOF) {
            throw new LexerException("No more tokens available.");
        }

        // skip the blank characters to reach the next token
        skipBlanks();

        // token type is EOF
        if (currentIndex >= data.length) {
            // update the token and also return it
            this.token = new Token(TokenType.EOF, null);
            return this.token;
        }

        // call the appropriate method for analyzing tokens, depending on the current state of the lexer
        return (state == LexerState.BASIC) ? nextTokenBasic() : nextTokenExtended();
    }

    /**
     * Processes and returns the next available token using basic lexical analysis.
     *
     * @return the next available token.
     * @throws LexerException if an error occurred while generating the next token.
     */
    private Token nextTokenBasic() throws LexerException {
        Token token;

        // token type is WORD
        if (Character.isLetter(data[currentIndex]) || data[currentIndex] == '\\') {
            String word = "";

            while (currentIndex < data.length) {
                char ch = data[currentIndex];

                if (Character.isLetter(ch)) {
                    word += ch;
                    currentIndex++;
                } else if (ch == '\\') {
                    int nextIndex = currentIndex + 1;

                    // if the escape symbol is at the very end (illegal)
                    // or if the escape sequence is not a number or a backslash
                    if (nextIndex >= data.length
                        || (!Character.isDigit(data[nextIndex]) && data[nextIndex] != '\\')) {
                        throw new LexerException("Illegal escape sequence.");
                    }

                    word += data[nextIndex];
                    currentIndex += 2;
                } else {
                    break;
                }
            }

            token = new Token(TokenType.WORD, word);

            // token type is NUMBER
        } else if (Character.isDigit(data[currentIndex])) {
            Long number = 0L;

            while (currentIndex < data.length) {
                if (!Character.isDigit(data[currentIndex])) break;

                int n = data[currentIndex] - '0';

                number *= 10;
                number += n;

                // if overflow happened, the input number was too large
                if (number < 0) {
                    throw new LexerException("The input number was too large to generate a token.");
                }
                currentIndex++;
            }

            token = new Token(TokenType.NUMBER, number);

            // token type is SYMBOL
        } else {
            token = new Token(TokenType.SYMBOL, data[currentIndex]);
            currentIndex++;
        }

        this.token = token;
        return token;
    }

    /**
     * Processes and returns the next available token using extended lexical analysis.
     *
     * @return the next available token.
     */
    private Token nextTokenExtended() {
        Token token;

        // token is anything except the toggle symbol
        if (data[currentIndex] != TOGGLE_SYMBOL) {
            String word = "";

            while (currentIndex < data.length) {
                char ch = data[currentIndex];

                // if the current character is one of the empty characters
                // or if it's the toggle symbol, then the whole word has been read
                if (EMPTY_CHARS.indexOf(ch) != -1 || ch == TOGGLE_SYMBOL) {
                    break;
                }

                word += ch;
                currentIndex++;
            }

            token = new Token(TokenType.WORD, word);

            // token is toggle symbol
        } else {
            token = new Token(TokenType.SYMBOL, TOGGLE_SYMBOL);
            currentIndex++;
        }

        this.token = token;
        return token;
    }

    /**
     * Returns a token that was last generated by the method {@link Lexer#nextToken}.
     *
     * @return the last generated token.
     */
    public Token getToken() {
        return token;
    }

    /**
     * Sets the new state for the lexer.
     *
     * @param state the new state for this lexer.
     */
    public void setState(LexerState state) {
        if (state == null) {
            throw new IllegalArgumentException("State cannot be null.");
        }
        this.state = state;
    }


    /**
     * Skips the empty characters in {@link Lexer#data} starting from currentIndex.
     */
    private void skipBlanks() {
        while (currentIndex < data.length) {
            // keep iterating the data sequence until the current character is not "empty"
            if (EMPTY_CHARS.indexOf(data[currentIndex]) == -1) break;
            currentIndex++;
        }
    }
}