package hr.fer.zemris.java.custom.scripting.parser;

import hr.fer.zemris.java.custom.scripting.elems.*;
import hr.fer.zemris.java.custom.scripting.lexer.Lexer;
import hr.fer.zemris.java.custom.scripting.lexer.LexerState;
import hr.fer.zemris.java.custom.scripting.lexer.Token;
import hr.fer.zemris.java.custom.scripting.lexer.TokenType;
import hr.fer.zemris.java.custom.scripting.nodes.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * This class represents a parser which parses the given 'smart script' and
 * creates the appropriate document model tree from it.
 *
 * @author Luka Čupić
 */
public class SmartScriptParser {

    /**
     * The lexer which will be used to generate tokens from the input document.
     */
    private Lexer lexer;

    /**
     * The root of the document model tree.
     */
    private DocumentNode root;

    /**
     * Creates a new parser and begins parsing the passed document.
     *
     * @param script the script to be parsed.
     * @throws SmartScriptParserException if a parsing error occurs.
     */
    public SmartScriptParser(String script) throws SmartScriptParserException {
        if (script == null) {
            throw new IllegalArgumentException();
        }

        lexer = new Lexer(script);
        root = new DocumentNode();

        try {
            parse();
        } catch (Exception ex) {
            throw new SmartScriptParserException(ex);
        }
    }

    /**
     * Processes the tokens obtained from the lexer.
     *
     * @throws SmartScriptParserException if a parsing error occurs.
     */
    private void parse() throws SmartScriptParserException {
        // A helper stack object for creating the document model tree.
        Stack<Node> stack = new Stack<>();

        stack.push(root);

        while (true) {
            Token token = lexer.nextToken();

            // if we've parsed the whole document
            if (token.getType() == TokenType.EOF) {
                break;
            }

            // if token is text
            if (token.getType() == TokenType.TEXT) {
                lexer.setState(LexerState.TEXT);
                TextNode textNode = parseText();

                (stack.peek()).addChildNode(textNode);

                // if token is tag start
            } else if (token.getType() == TokenType.TAG_START) {
                lexer.setState(LexerState.TAG);

                token = lexer.nextToken();

                if (token.getType() == TokenType.TAG_NAME || token.getType() == TokenType.VARIABLE) {
                    String tokenValue = token.getValue().toString().toLowerCase();

                    // for loop
                    if ("for".equals(tokenValue)) {
                        ForLoopNode forNode = parseForTag();

                        stack.peek().addChildNode(forNode);
                        stack.push(forNode);

                        // echo
                    } else if ("=".equals(tokenValue)) {
                        EchoNode echoNode = parseEchoTag();

                        (stack.peek()).addChildNode(echoNode);

                        // end tag
                    } else if ("end".equals(tokenValue)) {
                        token = lexer.nextToken();

                        if (token.getType() != TokenType.TAG_END) {
                            throw new SmartScriptParserException();
                        }

                        stack.pop();
                    }
                } else {
                    throw new SmartScriptParserException();
                }
                lexer.setState(LexerState.TEXT);
            } else {
                throw new SmartScriptParserException();
            }
        }

        if (stack.isEmpty()) {
            throw new SmartScriptParserException();
        }
    }

    /**
     * A helper method for parsing the text.
     *
     * @return a node representing the for-loop construct.
     */
    private TextNode parseText() {
        Token token = lexer.getToken();
        String text = token.getValue().toString();

        return new TextNode(text);
    }

    /**
     * A helper method for parsing the for-loop tag.
     *
     * @return a node representing the for-loop construct.
     * @throws SmartScriptParserException if a token is not recognized.
     */
    private ForLoopNode parseForTag() throws SmartScriptParserException {
        Token token = lexer.nextToken();

        if (token.getType() != TokenType.VARIABLE) {
            throw new SmartScriptParserException();
        }
        ElementVariable var = new ElementVariable(token.getValue().toString());

        token = lexer.nextToken();
        Element start = checkForLoopTokenType(token);

        token = lexer.nextToken();
        Element end = checkForLoopTokenType(token);

        token = lexer.nextToken();

        Element step = null;

        if (token.getType() == TokenType.TAG_END) {
            return new ForLoopNode(var, start, end, step);
        }

        step = checkForLoopTokenType(token);

        token = lexer.nextToken();

        if (token.getType() != TokenType.TAG_END) {
            throw new SmartScriptParserException();
        }

        return new ForLoopNode(var, start, end, step);
    }

    /**
     * Checks the type of a for-loop token and returns the appropriate element.
     *
     * @param token the token to check.
     * @return a new element object which corresponds to a passed token.
     * @throws SmartScriptParserException if a token is not recognized.
     */
    private Element checkForLoopTokenType(Token token) throws SmartScriptParserException {
        switch (token.getType()) {
            case VARIABLE:
                return new ElementVariable(token.getValue().toString());
            case INTEGER:
                return new ElementConstantInteger((int) token.getValue());
            case STRING:
                return new ElementString(token.getValue().toString());
            default:
                throw new SmartScriptParserException();
        }
    }

    /**
     * A helper method for parsing the echo tag.
     *
     * @return an EchoNode object representing the echo construct.
     * @throws SmartScriptParserException if a token is not recognized.
     */
    private EchoNode parseEchoTag() throws SmartScriptParserException {
        Token token = lexer.nextToken();

        // an array for storing the elements while parsing tokens
        List<Element> arrayElements = new ArrayList<>();

        while (token.getType() != TokenType.TAG_END) {
            switch (token.getType()) {
                case DOUBLE:
                    arrayElements.add(new ElementConstantDouble((double) token.getValue()));
                    break;
                case INTEGER:
                    arrayElements.add(new ElementConstantInteger((int) token.getValue()));
                    break;
                case FUNCTION:
                    arrayElements.add(new ElementFunction(token.getValue().toString()));
                    break;
                case OPERATOR:
                    arrayElements.add(new ElementOperator(token.getValue().toString()));
                    break;
                case STRING:
                    arrayElements.add(new ElementString(token.getValue().toString()));
                    break;
                case VARIABLE:
                    arrayElements.add(new ElementVariable(token.getValue().toString()));
                    break;
                default:
                    throw new SmartScriptParserException();
            }
            token = lexer.nextToken();
        }

        Element[] elements = new Element[arrayElements.size()];
        for (int i = 0, size = arrayElements.size(); i < size; i++) {
            elements[i] = arrayElements.get(i);
        }

        return new EchoNode(elements);
    }

    /**
     * Returns the parent node of the document model.
     *
     * @return the top-level node of the document model.
     */
    public DocumentNode getDocumentNode() {
        return root;
    }
}
