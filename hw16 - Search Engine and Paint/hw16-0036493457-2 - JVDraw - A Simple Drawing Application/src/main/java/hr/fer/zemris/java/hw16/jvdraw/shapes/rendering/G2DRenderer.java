package hr.fer.zemris.java.hw16.jvdraw.shapes.rendering;

import hr.fer.zemris.java.hw16.jvdraw.shapes.GeometricalObject;

import java.awt.*;

/**
 * This class provides a Graphics2D implementation of the Renderer
 * object, which renders the ({@link GeometricalObject})s onto the
 * Graphics2D object.
 *
 * @author Luka Čupić
 */
public class G2DRenderer implements Renderer {

	/**
	 * The Graphics2D object used for the actual rendering.
	 */
	private Graphics2D g2d;

	/**
	 * Creates a new instance of this class.
	 */
	public G2DRenderer() {
	}

	/**
	 * Creates a new instance of this class and sets up the
	 * Graphics2D object.L
	 */
	public G2DRenderer(Graphics2D g2d) {
		this.g2d = g2d;
	}

	public Graphics2D getG2d() {
		return g2d;
	}

	/**
	 * Sets the Graphics2D object used for rendering.
	 *
	 * @param g2d the Graphics2D object
	 */
	public void setG2d(Graphics2D g2d) {
		this.g2d = g2d;
	}

	@Override
	public void render(Point start, Point end, Color color) {
		g2d.setColor(color);
		g2d.drawLine(start.x, start.y, end.x, end.y);
	}

	@Override
	public void render(Point center, double radius, Color color) {
		g2d.setColor(color);
		g2d.setStroke(new BasicStroke(1.25f));

		int r = (int) radius;
		g2d.drawOval(center.x - r, center.y - r, 2 * r, 2 * r);
	}

	@Override
	public void render(Point center, double radius, Color color, Color fillColor) {
		g2d.setColor(fillColor);

		int r = (int) radius;
		g2d.fillOval(center.x - r, center.y - r, 2 * r, 2 * r);

		render(center, radius, color);
	}
}