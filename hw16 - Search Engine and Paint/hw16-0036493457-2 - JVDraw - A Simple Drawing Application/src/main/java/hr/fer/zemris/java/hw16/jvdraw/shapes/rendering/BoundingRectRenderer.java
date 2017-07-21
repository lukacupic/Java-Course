package hr.fer.zemris.java.hw16.jvdraw.shapes.rendering;

import hr.fer.zemris.java.hw16.jvdraw.model.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.shapes.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.shapes.Rectangle;

import java.awt.*;

/**
 * This class represents a bounding box renderer. It is used as
 * a Visitor of the {@link GeometricalObject}s available in the
 * {@link DrawingModel}; it searches for the bounding box (the
 * minimal box that encapsulates the whole image). The final
 * result is available through the method {@link #getRect()}.
 *
 * @author Luka Čupić
 */
public class BoundingRectRenderer implements Renderer {

	/**
	 * The x coordinate of the top left corner of the rectangle.
	 */
	private int xMin = Integer.MAX_VALUE;

	/**
	 * The y coordinate of the top left corner of the rectangle.
	 */
	private int yMin = Integer.MAX_VALUE;

	/**
	 * The x coordinate of the bottom right corner of the rectangle.
	 */
	private int xMax = Integer.MIN_VALUE;

	/**
	 * The y coordinate of the bottom right corner of the rectangle.
	 */
	private int yMax = Integer.MIN_VALUE;

	@Override
	public void render(Point start, Point end, Color color) {
		if (start.x < xMin) xMin = start.x;
		if (end.x < xMin) xMin = end.x;

		if (start.y < yMin) yMin = start.y;
		if (end.y < yMin) yMin = end.y;

		if (start.x > xMax) xMax = start.x;
		if (end.x > xMax) xMax = end.x;

		if (start.y > yMax) yMax = start.y;
		if (end.y > yMax) yMin = end.y;
	}

	@Override
	public void render(Point center, double radius, Color color) {
		int x1 = center.x - (int) radius;
		int y1 = center.y - (int) radius;
		int x2 = center.x + (int) radius;
		int y2 = center.y + (int) radius;

		if (x1 < xMin) xMin = x1;
		if (y1 < yMin) yMin = y1;
		if (x2 > xMax) xMax = x2;
		if (y2 > yMax) yMax = y2;
	}

	@Override
	public void render(Point center, double radius, Color color, Color fillColor) {
		render(center, radius, color);
	}

	/**
	 * Returns the minimal bounding rectangle that encapsulates the whole
	 * canvas (i.e. all objects on the canvas).
	 *
	 * @return the minimal bounding rectangle that encapsulates the whole
	 * canvas (i.e. all objects on the canvas).
	 */
	public Rectangle getRect() {
		return new Rectangle(xMin, yMin, xMax, yMax);
	}
}
