package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Represents an expression of a mathematical operator.
 *
 * @author Luka Čupić
 */
public class ElementOperator extends Element {

    /**
     * The value of the element.
     */
    private String symbol;

    /**
     * Creates a new element of type operator.
     *
     * @param symbol the value to be represented by this element.
     */
    public ElementOperator(String symbol) {
        if (symbol == null) {
            throw new IllegalArgumentException();
        }

        this.symbol = symbol;
    }

    /**
     * Returns the value of the element.
     *
     * @return the value of the element.
     */
    public String getSymbol() {
        return symbol;
    }

    @Override
    public String asText() {
        return symbol;
    }

}
