package hr.fer.zemris.java.hw16.jvdraw.shapes;

import hr.fer.zemris.java.hw16.jvdraw.shapes.rendering.Renderer;

import java.awt.*;

/**
 * This class represents a line. It encapsulates the
 * start point, the end point and the color of the line.
 *
 * @author Luka Čupić
 */
public class Line extends GeometricalObject {

	/**
	 * A static counter for the total number of {@link Line}
	 * objects created.
	 */
	private static volatile int numberOfLines;

	/**
	 * The ordinal number of this {@link Line} object, in respect
	 * to the total number of lines ({@link #numberOfLines}).
	 */
	private int thisLine;

	/**
	 * The start point of the line.
	 */
	private Point start;

	/**
	 * The end point of the line.
	 */
	private Point end;

	/**
	 * The color of the line.
	 */
	private Color color;

	/**
	 * Creates a new instance of this class.
	 *
	 * @param start the first point of the line
	 * @param end   the last point of the line
	 * @param color the color of the line
	 */
	public Line(Point start, Point end, Color color) {
		this.start = start;
		this.end = end;
		this.color = color;

		numberOfLines++;
		thisLine = numberOfLines;
	}

	@Override
	public void render(Renderer r) {
		r.render(start, end, color);
	}

	/**
	 * Gets the start point of the line.
	 *
	 * @return the starting point of the line
	 */
	public Point getStart() {
		return start;
	}

	/**
	 * Sets the start point of the line.
	 *
	 * @param start the new starting point of the line
	 */
	public void setStart(Point start) {
		this.start = start;
	}


	/**
	 * Gets the ending point of the line.
	 *
	 * @return the ending point of the line
	 */
	public Point getEnd() {
		return end;
	}

	/**
	 * Sets the ending point of the line.
	 *
	 * @param end the new ending point of the line
	 */
	public void setEnd(Point end) {
		this.end = end;
	}

	/**
	 * Gets the color of the line.
	 *
	 * @return the color of the line
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Sets the color of the line.
	 *
	 * @param color the new color of the line
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return "Line " + thisLine;
	}
}
