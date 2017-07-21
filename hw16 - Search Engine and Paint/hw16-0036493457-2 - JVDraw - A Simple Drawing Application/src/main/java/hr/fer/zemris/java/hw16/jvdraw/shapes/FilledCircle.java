package hr.fer.zemris.java.hw16.jvdraw.shapes;

import hr.fer.zemris.java.hw16.jvdraw.shapes.rendering.Renderer;

import java.awt.*;

/**
 * This class represents a filled circle (a circle with
 * both the outline color and the fill color). The object
 * encapsulates the center point, the radius, the color
 * of the border and the filling color.
 * The circle has no filling color; for a filled circle,
 * see {@link FilledCircle}.
 *
 * @author Luka Čupić
 */
public class FilledCircle extends Circle {

	/**
	 * The fill color of the circle.
	 */
	private Color fillColor;

	/**
	 * Creates a new instance of this class.
	 *
	 * @param center    the center of the circle
	 * @param radius    the radius of the circle
	 * @param color     the color of the circle
	 * @param fillColor the fill color of the circle
	 */
	public FilledCircle(Point center, double radius, Color color, Color fillColor) {
		super(center, radius, color);
		this.fillColor = fillColor;
	}

	@Override
	public void render(Renderer r) {
		r.render(getCenter(), getRadius(), super.getColor(), fillColor);
	}

	/**
	 * Gets the fill color of the circle.
	 *
	 * @return the fill color of the circle
	 */
	public Color getFillColor() {
		return fillColor;
	}

	/**
	 * Sets the fill color of the circle.
	 *
	 * @param fillColor the fill color of the circle
	 */
	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}
}
