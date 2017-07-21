package hr.fer.zemris.java.gui.charts;

/**
 * This class represents a pair of X and Y values which
 * represent a point of the coordinate system.
 *
 * @author Luka Čupić
 */
public class XYValue {

    /**
     * Represents the x value.
     */
    private int x;

    /**
     * Represents the y value.
     */
    private int y;

    /**
     * Creates a new instance of this class.
     *
     * @param x the x value to set
     * @param y the y value to set
     */
    public XYValue(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the x value.
     *
     * @return the x value of this object.
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y value.
     *
     * @return the y value of this object.
     */
    public int getY() {
        return y;
    }
}
