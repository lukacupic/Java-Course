package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Represents an expression of an integer.
 *
 * @author Luka Čupić
 */
public class ElementConstantInteger extends Element {

    /**
     * The value of the element.
     */
    private int value;

    /**
     * Creates a new element of type integer.
     *
     * @param value the value to be represented by this element.
     */
    public ElementConstantInteger(int value) {
        this.value = value;
    }

    /**
     * Returns the value of the element.
     *
     * @return the value of the element.
     */
    public int getValue() {
        return value;
    }

    @Override
    public String asText() {
        return String.valueOf(value);
    }
}
