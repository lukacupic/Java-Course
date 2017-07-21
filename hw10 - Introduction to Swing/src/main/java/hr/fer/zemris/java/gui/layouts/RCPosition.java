package hr.fer.zemris.java.gui.layouts;

/**
 * This class represents immutable objects which
 * encapsulate information about a position in a table.
 * <p>
 * Each instance of this class contains row and column
 * attributes which specify the number of the row and
 * the number of the column.
 *
 * @author Luka Čupić
 */
public class RCPosition {

    /**
     * Represents the row number.
     */
    private int row;

    /**
     * Represents the column number.
     */
    private int column;

    /**
     * Creates a new instance of this class.
     *
     * @param row    the row number
     * @param column the column number
     */
    public RCPosition(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Gets the row represented by this object.
     *
     * @return the row represented by this object.
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets the column represented by this object.
     *
     * @return the column represented by this object.
     */
    public int getColumn() {
        return column;
    }
}
