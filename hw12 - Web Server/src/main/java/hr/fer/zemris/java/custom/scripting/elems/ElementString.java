package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Represents an expression of a string.
 *
 * @author Luka Čupić
 */
public class ElementString extends Element {

    /**
     * The value of the element.
     */
    private String value;

    /**
     * Creates a new element of type string.
     *
     * @param value the value to be represented by this element.
     */
    public ElementString(String value) {
        if (value == null) {
            throw new IllegalArgumentException();
        }

        this.value = value;
    }

    /**
     * Returns the value of the element.
     *
     * @return the value of the element.
     */
    public String getValue() {
        return value;
    }

    @Override
    public String asText() {
        return value;
    }
}
