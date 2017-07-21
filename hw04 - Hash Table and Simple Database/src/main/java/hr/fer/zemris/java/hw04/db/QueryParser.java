package hr.fer.zemris.java.hw04.db;

import hr.fer.zemris.java.hw04.db.lexer.QueryLexer;
import hr.fer.zemris.java.hw04.db.lexer.Token;
import hr.fer.zemris.java.hw04.db.lexer.TokenType;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a parser for the database query command.
 * <p>
 * The only supported command is "query" which is used in the following way:
 * <p>
 * <pre><code>query [attribute] [operator] [value]</code></pre>
 *
 * In the defined structure, the accepted attributes are as defined
 * in the {@link StudentRecord}; all other attributes are considered
 * illegal and will not be parsed!
 *
 * @author Luka Čupić
 */
public class QueryParser {

    /**
     * The lexer which will be used to generate tokens from the input document.
     */
    private QueryLexer lexer;

    /**
     * Holds the conditional expressions parsed from the input.
     */
    private List<ConditionalExpression> expressionList;

    /**
     * Creates a new parser object and begins parsing the input text.
     *
     * @param data the input text to be parsed.
     * @throws QueryParserException if a parsing error occurs.
     */
    public QueryParser(String data) throws QueryParserException {
        lexer = new QueryLexer(data);
        expressionList = new ArrayList<>();

        try {
            parse();
        } catch (QueryParserException ex) {
            throw new QueryParserException(ex);
        } catch (Exception ex) {
            throw new QueryParserException("An unknown error has occurred while parsing the text!");
        }
    }

    /**
     * When called, begins processing tokens obtained from the lexer.
     *
     * @throws QueryParserException if a parsing error occurs.
     */
    private void parse() throws QueryParserException {
        // declared here because nextToken() is called at the end
        // of the while(true) loop except for the first time


        while (true) {
            Token oldToken = lexer.getToken();
            Token token = lexer.nextToken();

            // if we've parsed the whole document
            if (token.getType() == TokenType.EOF) {
                // check if the previous token was null
                if (oldToken == null) {
                    throw new QueryParserException("Query must not be empty!");
                }

                // check if the previous token was logical operator
                if (oldToken.getType() == TokenType.LOGICAL_OPERATOR) {
                    throw new QueryParserException("New expression expected!");
                }

                break;
            }

            // first token in each expression must be an attribute!
            if (token.getType() != TokenType.ATTRIBUTE) {
                throw new QueryParserException("Inappropriate token! Attribute expected!");
            }
            IFieldValueGetter attribute = parseAttribute(token);

            token = lexer.nextToken();

            // second token in each expression must be a conditional operator!
            if (token.getType() != TokenType.CONDITIONAL_OPERATOR) {
                throw new QueryParserException("Inappropriate token! Conditional operator expected!");
            }
            IComparisonOperator operator = parseComparisonOperator(token);

            token = lexer.nextToken();

            // third token in each expression must be a string literal!
            if (token.getType() != TokenType.STRING) {
                throw new QueryParserException("Inappropriate token! String literal expected!");
            }
            String literal = (String) token.getValue();

            // create a new conditional expression from the parsed values and add it to the list
            expressionList.add(new ConditionalExpression(attribute, literal, operator));

            token = lexer.nextToken();

            // fourth token is EOF
            if (token.getType() == TokenType.EOF) break;

            // if fourth token is not a logical operator, then it's illegal
            if (token.getType() != TokenType.LOGICAL_OPERATOR) {
                throw new QueryParserException("Inappropriate token! Logical operator expected!");
            }
        }
    }

    /**
     * A helper method used for parsing an attribute token.
     *
     * @param token the attribute token to parse.
     * @return a field value of the attribute.
     * @throws QueryParserException if the attribute name is not legal.
     */
    private IFieldValueGetter parseAttribute(Token token) throws QueryParserException {
        String value = (String) token.getValue();

        if ("jmbag".equals(value)) {
            return FieldValueGetters.JMBAG;
        }

        if ("lastName".equals(value)) {
            return FieldValueGetters.LAST_NAME;
        }

        if ("firstName".equals(value)) {
            return FieldValueGetters.FIRST_NAME;
        }

        throw new QueryParserException("Illegal attribute name!");
    }

    /**
     * A helper method used for parsing a comparison operator token.
     *
     * @param token the comparison operator token to parse.
     * @return a field value of the comparison operator.
     * @throws QueryParserException if the comparison operator is not legal.
     */
    private IComparisonOperator parseComparisonOperator(Token token) throws QueryParserException {
        String value = (String) token.getValue();

        if ("<".compareTo(value) == 0) {
            return ComparisonOperators.LESS;
        }

        if (">".compareTo(value) == 0) {
            return ComparisonOperators.GREATER;
        }

        if ("=".compareTo(value) == 0) {
            return ComparisonOperators.EQUALS;
        }

        if ("<=".compareTo(value) == 0) {
            return ComparisonOperators.LESS_OR_EQUAL;
        }

        if (">=".compareTo(value) == 0) {
            return ComparisonOperators.GREATER_OR_EQUAL;
        }

        if ("!=".compareTo(value) == 0) {
            return ComparisonOperators.NOT_EQUAL;
        }

        if ("LIKE".compareTo(value) == 0) {
            return ComparisonOperators.LIKE;
        }

        throw new QueryParserException("Illegal comparison operator!");
    }

    /**
     * Returns true if and only if the parsed query was in the form
     * <pre><code>jmbag="xxx"</code></pre>; so called "direct query".
     *
     * @return true if the query was a direct query; false otherwise.
     */
    public boolean isDirectQuery() {
        if (expressionList.size() != 1) return false;

        ConditionalExpression expr = expressionList.get(0);

        if (expr.getFieldGetter() != FieldValueGetters.JMBAG) return false;
        if (expr.getComparisonOperator() != ComparisonOperators.EQUALS) return false;

        return true;

    }

    /**
     * Returns a string literal of the direct query comparison.
     *
     * @return a string literal of the direct query comparison.
     * @throws IllegalStateException if the parsed query was not a direct query.
     */
    public String getQueriedJMBAG() throws IllegalStateException {
        if (!isDirectQuery()) throw new IllegalStateException("The query was not direct!");

        return expressionList.get(0).getStringLiteral();
    }

    /**
     * Returns a list of all the conditional expressions parsed from this query.
     *
     * @return a list of conditional expressions of type {@link ConditionalExpression}.
     */
    public List<ConditionalExpression> getQuery() {
        return expressionList;
    }
}