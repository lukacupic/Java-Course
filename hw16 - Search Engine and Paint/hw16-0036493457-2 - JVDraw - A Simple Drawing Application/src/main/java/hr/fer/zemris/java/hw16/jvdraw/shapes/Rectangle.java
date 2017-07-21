package hr.fer.zemris.java.hw16.jvdraw.shapes;

/**
 * This class represents a rectangle. It encapsulates the
 * coordinates of the top-left corner, and the coordinates
 * of the bottom-right corner of the rectangle.
 *
 * @author Luka Čupić
 */
public class Rectangle {

	/**
	 * The x coordinate of the top left corner of the rectangle.
	 */
	private int x1;

	/**
	 * The y coordinate of the top left corner of the rectangle.
	 */
	private int y1;

	/**
	 * The x coordinate of the bottom right corner of the rectangle.
	 */
	private int x2;

	/**
	 * The y coordinate of the bottom right corner of the rectangle.
	 */
	private int y2;

	/**
	 * Creates a new {@link Rectangle} object.
	 *
	 * @param x1 the x coordinate of the top left corner of the rectangle
	 * @param y1 the y coordinate of the top left corner of the rectangle
	 * @param x2 the x coordinate of the bottom right corner of the rectangle
	 * @param y2 the y coordinate of the bottom right corner of the rectangle
	 */
	public Rectangle(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	/**
	 * Gets the x coordinate of the top left corner of the rectangle
	 *
	 * @return the x coordinate of the top left corner of the rectangle
	 */
	public int getX1() {
		return x1;
	}

	/**
	 * Gets the y coordinate of the top left corner of the rectangle
	 *
	 * @return the y coordinate of the top left corner of the rectangle
	 */
	public int getY1() {
		return y1;
	}

	/**
	 * Gets the x coordinate of the bottom right corner of the rectangle
	 *
	 * @return the x coordinate of the bottom right corner of the rectangle
	 */
	public int getX2() {
		return x2;
	}

	/**
	 * Gets the y coordinate of the bottom right corner of the rectangle
	 *
	 * @return the y coordinate of the bottom right corner of the rectangle
	 */
	public int getY2() {
		return y2;
	}
}