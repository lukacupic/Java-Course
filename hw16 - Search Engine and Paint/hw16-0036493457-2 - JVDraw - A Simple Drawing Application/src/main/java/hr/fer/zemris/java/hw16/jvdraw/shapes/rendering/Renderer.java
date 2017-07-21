package hr.fer.zemris.java.hw16.jvdraw.shapes.rendering;

import hr.fer.zemris.java.hw16.jvdraw.shapes.GeometricalObject;

import java.awt.*;

/**
 * This class represents a renderer of {@link GeometricalObject}s
 * onto the provided visual medium. A Renderer contains one method
 * for each of the distinct geometric objects. The implementor of
 * this class must provide concrete implementations of each of the
 * methods, so that each of the distinct {@link GeometricalObject}
 * types is able to be rendered onto the medium.
 *
 * @author Luka Čupić
 */
public interface Renderer {

	/**
	 * Renders a line onto the visual medium.
	 *
	 * @param start the starting point of the line
	 * @param end   the ending point of the line
	 * @param color the color of the line
	 */
	void render(Point start, Point end, Color color);

	/**
	 * Renders a circle onto the visual medium.
	 *
	 * @param center the center point of the circle
	 * @param radius the radius of the circle
	 * @param color  the color of the outline
	 */
	void render(Point center, double radius, Color color);

	/**
	 * Renders a circle onto the visual medium.
	 *
	 * @param center    the center point of the circle
	 * @param radius    the radius of the circle
	 * @param color     the color of the outline
	 * @param fillColor the color of the area
	 */
	void render(Point center, double radius, Color color, Color fillColor);
}