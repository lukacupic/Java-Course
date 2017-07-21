package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Performs a lexical analysis of a sequence of characters and converts it into a sequence of tokens.
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
     * Creates a new Lexer object and stores the input text {@code text} which
     * will be used as the sequence of characters to analyze.
     *
     * @param text the sequence of characters to convert to tokens.
     */
    public Lexer(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Input cannot be null.");
        }
        data = text.toCharArray();
        state = LexerState.TEXT;
    }

    /**
     * Processes and returns the next available token.
     *
     * @return the next available token.
     * @throws LexerException if an error occurs while generating a token.
     */
    public Token nextToken() throws LexerException {
        // if there are no more tokens
        if (token != null && token.getType() == TokenType.EOF) {
            throw new LexerException("No more tokens available.");
        }

        // token type is EOF
        if (currentIndex >= data.length) {
            this.token = new Token(TokenType.EOF, null);
            return this.token;
        }

        // call the appropriate method for analyzing tokens, depending on the current state of the lexer
        // also, if any exception happens, rethrow it as LexerException
        try {
            Token t = state == LexerState.TEXT ? nextTextToken() : nextTagToken();
            return t;
        } catch (Exception ex) {
            throw new LexerException();
        }
    }

    /**
     * Processes and returns the next available token from the text part of the document.
     *
     * @return the next available text token.
     * @throws LexerException if an error occurs while generating a token.
     */
    private Token nextTextToken() {
        Token token;

        if (data[currentIndex] != '{' || data[currentIndex + 1] != '$') {
            StringBuilder textBuilder = new StringBuilder();

            while (currentIndex < data.length) {
                if (data[currentIndex] == '{' && data[currentIndex + 1] == '$') {
                    break;
                }

                // if we've stumbled upon a wild escape sequence
                if (data[currentIndex] == '\\') {
                    if (data[currentIndex + 1] == '\\') {
                        textBuilder.append("\\");
                        currentIndex += 2;
                        continue;
                    } else if (data[currentIndex + 1] == '{') {
                        textBuilder.append("{");
                        currentIndex += 2;
                        continue;
                    } else {
                        throw new LexerException();
                    }
                }

                textBuilder.append(data[currentIndex++]);
            }
            token = new Token(TokenType.TEXT, textBuilder.toString());
        } else {
            currentIndex += 2;
            token = new Token(TokenType.TAG_START, null);
        }

        this.token = token;
        return token;
    }

    /**
     * Processes and returns the next available token from the tag part of the document.
     *
     * @return the next available tag token.
     * @throws LexerException if an error occurs while generating a token.
     */
    private Token nextTagToken() {
        Token token;

        // if we've not yet reached the tag end
        if (data[currentIndex] != '$' || data[currentIndex + 1] != '}') {
            skipBlanks();

            // we've encountered a variable
            if (Character.isLetter(data[currentIndex])) {

                String var = parseVariableName();

                if (!isLegalName(var)) {
                    throw new LexerException();
                }

                token = new Token(TokenType.VARIABLE, var);

                // we've encountered a string
            } else if (data[currentIndex] == '"') {
                currentIndex++; // skip the first quotation mark
                StringBuilder stringBuilder = new StringBuilder();

                while (currentIndex < data.length) {
                    if (data[currentIndex] == '"') {
                        currentIndex++; // skip the last quotation mark
                        break;
                    }

                    // if we've stumbled upon a wild escape sequence
                    if (data[currentIndex] == '\\') {
                        if (data[currentIndex + 1] == '\\') {
                            stringBuilder.append("\\");
                            currentIndex += 2;
                            continue;
                        } else if (data[currentIndex + 1] == '"') {
                            stringBuilder.append("\"");
                            currentIndex += 2;
                            continue;
                        } else if (data[currentIndex + 1] == 'n') {
                            stringBuilder.append("\n");
                            currentIndex += 2;
                            continue;
                        } else if (data[currentIndex + 1] == 'r') {
                            stringBuilder.append("\r");
                            currentIndex += 2;
                            continue;
                        } else if (data[currentIndex + 1] == 't') {
                            stringBuilder.append("\t");
                            currentIndex += 2;
                            continue;
                        } else {
                            throw new LexerException();
                        }
                    }

                    stringBuilder.append(data[currentIndex++]);
                }

                token = new Token(TokenType.STRING, stringBuilder.toString());

                // we've encountered an operator (please don't look at the conditional logic here)
            } else if ("+-*/^".indexOf(data[currentIndex]) != -1
                && (data[currentIndex] != '-' || !Character.isDigit(data[currentIndex + 1]))) {
                token = new Token(TokenType.OPERATOR, data[currentIndex++]);

                // we've encountered a (negative) number
            } else if (Character.isDigit(data[currentIndex]) || data[currentIndex] == '-') {
                StringBuilder numBuilder = new StringBuilder();
                boolean isDouble = false; // assume the number is integer by default

                while (currentIndex < data.length) {
                    // if char is an empty character, or an end tag, the number has been read
                    if ("\r\n\t ".indexOf(data[currentIndex]) != -1
                        || (data[currentIndex] == '$'
                        && data[currentIndex + 1] == '}')) {
                        break;
                    }

                    // if the char is not a digit or a (decimal) dot or a negative sign, or an end tag, error!
                    if (!Character.isDigit(data[currentIndex])
                        && data[currentIndex] != '.'
                        && data[currentIndex] != '-') {
                        throw new LexerException();
                    }

                    // if the number contains a dot, then it's a double
                    if (data[currentIndex] == '.') {
                        // if there are no digits after the dot, the number is illegal
                        if (!Character.isDigit(data[currentIndex + 1])) {
                            throw new LexerException();
                        }
                        isDouble = true;
                    }

                    numBuilder.append(data[currentIndex++]);
                }

                try {
                    // if it's an integer, parse it as an integer
                    if (!isDouble) {
                        int num = Integer.parseInt(numBuilder.toString());
                        token = new Token(TokenType.INTEGER, num);

                        // if it's a double, parse it as a double
                    } else {
                        double num = Double.parseDouble(numBuilder.toString());
                        token = new Token(TokenType.DOUBLE, num);
                    }
                } catch (NumberFormatException ex) {
                    throw new LexerException();
                }


                // we've encountered a function
            } else if (data[currentIndex] == '@') {
                currentIndex++; //ignore the function symbol

                String var = parseVariableName();

                if (!isLegalName(var)) {
                    throw new LexerException();
                }

                token = new Token(TokenType.FUNCTION, var);

                // we've encountered the tag end
            } else if (data[currentIndex] == '$' && data[currentIndex + 1] == '}') {
                token = new Token(TokenType.TAG_END, null);
                currentIndex += 2;

                // we've encountered a tag name
            } else if (data[currentIndex] == '=') {
                token = new Token(TokenType.TAG_NAME, data[currentIndex++]);

                // token not recognized - throw an exception
            } else {
                throw new LexerException();
            }

            // if we've reached the tag end, that is the token
        } else {
            token = new Token(TokenType.TAG_END, null);
            currentIndex += 2;
        }

        this.token = token;
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
     * Returns a token that was last generated by the method {@link Lexer#nextToken}.
     *
     * @return the last generated token.
     */
    public Token getToken() {
        return token;
    }

    /**
     * Checks if the passed string is a legal variable name.
     * A legal variable name starts with a letter and contains only letters, numbers and underscores.
     *
     * @return true if the string is a legal name; false otherwise.
     */
    private boolean isLegalName(String string) {
        if (string == null) {
            throw new IllegalArgumentException();
        }

        // converts the string to a char array for easier iteration
        char[] chars = string.toCharArray();

        // if the first character is not a letter
        if (!Character.isLetter(chars[0])) {
            return false;
        }

        for (char c : chars) {
            if (Character.isLetter(c)) continue;
            if (Character.isDigit(c)) continue;
            if (c == '_') continue;

            return false;
        }
        return true;
    }

    /**
     * Skips the empty characters in data starting from currentIndex.
     * Empty characters include "carriage return", "linfe feed", "tab" and "space".
     */
    private void skipBlanks() {
        while (currentIndex < data.length) {
            // keep iterating the data sequence until the current character is not "empty"
            if ("\r\n\t ".indexOf(data[currentIndex]) == -1) break;
            currentIndex++;
        }
    }

    /**
     * Parses the name of the variable in the data array.
     *
     * @return the variable name
     */
    private String parseVariableName() {
        StringBuilder builder = new StringBuilder();

        while (currentIndex < data.length) {
            // if the current character is an invalid variable name or if we've reached
            // the tag end
            if ("\r\n\t ".indexOf(data[currentIndex]) != -1
                || (data[currentIndex] == '$' && data[currentIndex + 1] == '}')) {
                break;
            }

            // ... all other characters are illegal
            if (!Character.isLetter(data[currentIndex])
                && !Character.isDigit(data[currentIndex])
                && data[currentIndex] != '_') {
                throw new LexerException();
            }

            builder.append(data[currentIndex++]);
        }
        return builder.toString();
    }
}