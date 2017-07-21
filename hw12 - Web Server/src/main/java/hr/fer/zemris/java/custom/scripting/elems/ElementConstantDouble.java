package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Represents an expression of a double number.
 *
 * @author Luka Čupić
 */
public class ElementConstantDouble extends Element {

    /**
     * The value of the element.
     */
    private double value;

    /**
     * Creates a new element of type double.
     *
     * @param value the value to be represented by this element.
     */
    public ElementConstantDouble(double value) {
        this.value = value;
    }

    /**
     * Returns the value of the element.
     *
     * @return the value of the element.
     */
    public double getValue() {
        return value;
    }

    @Override
    public String asText() {
        return String.valueOf(value);
    }
}
