package hr.fer.zemris.java.hw16.jvdraw.shapes;

import hr.fer.zemris.java.hw16.jvdraw.shapes.rendering.Renderer;

import java.awt.*;
import java.text.DecimalFormat;

/**
 * This class represents a circle. It encapsulates the
 * center point, the radius and the color of the circle.
 * The circle has no filling color; for a filled circle,
 * see {@link FilledCircle}.
 *
 * @author Luka Čupić
 */
public class Circle extends GeometricalObject {

	/**
	 * A static counter for the total number of {@link Circle}
	 * objects created.
	 */
	private static volatile int numberOfCircles;

	/**
	 * The ordinal number of this {@link Circle} object, in respect
	 * to the total number of circles ({@link #numberOfCircles}).
	 */
	private int thisCircle;

	/**
	 * The center of the circle.
	 */
	private Point center;

	/**
	 * The radius of the circle.
	 */
	private double radius;

	/**
	 * The color of the circle.
	 */
	private Color color;

	/**
	 * Creates a new instance of this class.
	 *
	 * @param center the center of the circle
	 * @param radius the radius of the circle
	 * @param color  the color of the circle
	 */
	public Circle(Point center, double radius, Color color) {
		this.center = center;
		this.radius = radius;
		this.color = color;

		numberOfCircles++;
		thisCircle = numberOfCircles;
	}

	@Override
	public void render(Renderer r) {
		r.render(center, radius, color);
	}

	/**
	 * Gets the center point of the circle.
	 *
	 * @return the center point of the circle.
	 */
	public Point getCenter() {
		return center;
	}

	/**
	 * Sets the center point of the circle.
	 *
	 * @param center the new center point of the circle.
	 */
	public void setCenter(Point center) {
		this.center = center;
	}

	/**
	 * Gets the radius of the circle.
	 *
	 * @return the radius of the circle.
	 */
	public double getRadius() {
		DecimalFormat f = new DecimalFormat("#0.0000");
		return Double.parseDouble(f.format(radius).replace(",", "."));
	}

	/**
	 * Sets the radius of the circle.
	 *
	 * @param radius the new radius of the circle.
	 */
	public void setRadius(double radius) {
		this.radius = radius;
	}

	/**
	 * Gets the color of the circle.
	 *
	 * @return the color of the circle
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Sets the color of the circle.
	 *
	 * @param color the new color of the circle
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return "Circle " + thisCircle;
	}
}
