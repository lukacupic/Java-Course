package hr.fer.zemris.bf.parser;

import hr.fer.zemris.bf.lexer.Lexer;
import hr.fer.zemris.bf.lexer.TokenType;
import hr.fer.zemris.bf.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a parser for boolean expressions which works with basic boolean functions
 * such as: 'and', 'or', 'xor' and 'not', representing logical conjunction, logical
 * disjunction, exclusive logical disjunction and logical negation respectively.
 * <p>
 * The parser accepts the following grammar:
 * <p>
 * S -> E1
 * E1 -> E2 (OR E2)*
 * E2 -> E3 (XOR E3)*
 * E3 -> E4 (AND E4)*
 * E4 -> NOT E4 | E5
 * E5 -> VAR | CONST | '(' E1 ')'
 * <p>
 * In the defined grammar, S represents the first non-terminal symbol and E1, E2, E3, E4
 * and E5 represent the remaining non-terminal symbols.
 * VAR and CONST are terminal symbols which represent a variable and a constant respectively.
 * A variable consists of letters, digits and underscores, where the first character must 
 * be a character. 
 * A constant represent a single boolean value and can be denoted numerically (1 and 0) or 
 * alphabetically (true and false).
 *
 * @author Luka Čupić
 */
public class Parser {

    /**
     * The lexer which will be used to generate tokens from the given expression.
     */
    private Lexer lexer;

    /**
     * The root of the model tree.
     */
    private Node root;

    /**
     * Creates a new parser and begins parsing the passed expression.
     *
     * @param expression the boolean expression to parse
     * @throws ParserException if a parsing error occurs.
     */
    public Parser(String expression) throws ParserException {
        if (expression == null) {
            throw new ParserException("Illegal expression!");
        }

        lexer = new Lexer(expression);

        try {
            parse();
        } catch (Exception ex) {
            throw new ParserException(ex);
        }
    }

    /**
     * Checks if the current token (i.e. the last generated token by the
     * lexer) is of the given type.
     *
     * @param type the type to check
     * @return true if the current token is of the given type; false otherwise
     */
    private boolean isTokenOfType(TokenType type) {
        return lexer.getToken().getTokenType() == type;
    }

    /**
     * Checks if the current token (i.e. the last generated token by the
     * lexer) is of the given value
     *
     * @param value the value to check
     * @return true if the current token is of the given value; false otherwise
     */
    private boolean isTokenOfValue(Object value) {
        return value.equals(lexer.getToken().getTokenValue());
    }

    /**
     * Begins parsing the expression given to the parser.
     *
     * @throws ParserException if a parsing error occurs.
     */
    private void parse() throws ParserException {
        root = E1();

        if (!isTokenOfType(TokenType.EOF)) {
            throw new ParserException("Unexpected token!");
        }
    }

    /**
     * A helper method for parsing the E1 production of this parser's
     * grammar. The method parses a non-terminal symbol E2 and an arbitrary
     * number (which can be zero) of (OR E2) pairs.
     *
     * @return a node which holds the tree model of the E1 production
     * @throws ParserException if a parsing error occurs
     */
    private Node E1() throws ParserException {

        Node first = E2();

        if (!isTokenOfType(TokenType.OPERATOR) || !isTokenOfValue("or")) {
            return first;
        }

        List<Node> children = new ArrayList<>();
        children.add(first);

        while (isTokenOfType(TokenType.OPERATOR) && isTokenOfValue("or")) {
            lexer.nextToken();
            children.add(E2());
        }
        return new BinaryOperatorNode("or", children, Boolean::logicalOr);
    }

    /**
     * A helper method for parsing the E2 production of this parser's
     * grammar. The method parses a non-terminal symbol E3 and an arbitrary
     * number (which can be zero) of (XOR E3) pairs.
     *
     * @return a node which holds the tree model of the E2 production
     * @throws ParserException if a parsing error occurs
     */
    private Node E2() {
        Node first = E3();

        if (!isTokenOfType(TokenType.OPERATOR) || !isTokenOfValue("xor")) {
            return first;
        }

        List<Node> children = new ArrayList<>();
        children.add(first);

        while (isTokenOfType(TokenType.OPERATOR) && isTokenOfValue("xor")) {
            lexer.nextToken();
            children.add(E3());
        }
        return new BinaryOperatorNode("xor", children, Boolean::logicalXor);
    }

    /**
     * A helper method for parsing the E3 production of this parser's
     * grammar. The method parses a non-terminal symbol E4 and an arbitrary
     * number (which can be zero) of (AND E4) pairs.
     *
     * @return a node which holds the tree model of the E3 production
     * @throws ParserException if a parsing error occurs
     */
    private Node E3() {
        Node first = E4();

        if (!isTokenOfType(TokenType.OPERATOR) || !isTokenOfValue("and")) {
            return first;
        }

        List<Node> children = new ArrayList<>();
        children.add(first);

        while (isTokenOfType(TokenType.OPERATOR) && isTokenOfValue("and")) {
            lexer.nextToken();
            children.add(E4());
        }
        return new BinaryOperatorNode("and", children, Boolean::logicalAnd);
    }

    /**
     * A helper method for parsing the E4 production of this parser's
     * grammar. The method parses a negated non-terminal symbol E4 or
     * a non-terminal symbol E5.
     *
     * @return a node which holds the tree model of the E4 production
     * @throws ParserException if a parsing error occurs
     */
    private Node E4() {
        if (isTokenOfType(TokenType.OPERATOR) && isTokenOfValue("not")) {
            lexer.nextToken();
            return new UnaryOperatorNode("not", E4(), a -> !a);
        }
        return E5();
    }

    /**
     * A helper method for parsing the E5 production of this parser's
     * grammar. This method accepts only the following symbols:
     * a variable, a constant or a non-terminal symbol surrounded by
     * rounded brackets '(' and ')'. All other symbols are considered
     * illegal and an appropriate exception will be thrown if any of
     * them are encountered.
     *
     * @return a node which holds the tree model of the E5 production
     * @throws ParserException if a parsing error occurs
     */
    private Node E5() {

        if (isTokenOfType(TokenType.VARIABLE)) {
            String var = (String) lexer.getToken().getTokenValue();
            lexer.nextToken();
            return new VariableNode(var);

        } else if (isTokenOfType(TokenType.CONSTANT)) {
            Boolean constant = (Boolean) lexer.getToken().getTokenValue();
            lexer.nextToken();
            return new ConstantNode(constant);

        } else if (isTokenOfType(TokenType.OPEN_BRACKET)) {
            lexer.nextToken();
            Node nested = E1();

            if (!isTokenOfType(TokenType.CLOSED_BRACKET)) {
                throw new ParserException("Unexpected token!");
            }
            lexer.nextToken();
            return nested;

        } else {
            throw new ParserException("Unexpected token!");
        }
    }

    /**
     * Returns a model tree which represents the parsed expression.
     *
     * @return the root node of the model tree representing the parsed expression.
     */
    public Node getExpression() {
        return root;
    }
}
