package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Represents an expression of a mathematical function.
 *
 * @author Luka Čupić
 */
public class ElementFunction extends Element {

    /**
     * The value of the element.
     */
    private String name;

    /**
     * Creates a new element of type function.
     *
     * @param name the value to be represented by this element.
     */
    public ElementFunction(String name) {
        if (name == null) {
            throw new IllegalArgumentException();
        }

        this.name = name;
    }

    /**
     * Returns the value of the element.
     *
     * @return the value of the element.
     */
    public String getName() {
        return name;
    }

    @Override
    public String asText() {
        return name;
    }
}
