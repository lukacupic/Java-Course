package hr.fer.zemris.java.hw16.jvdraw.shapes.rendering;

import hr.fer.zemris.java.hw16.jvdraw.shapes.GeometricalObject;

import java.awt.*;

/**
 * This class provides an implementation of the Renderer object, which
 * renders the ({@link GeometricalObject})s onto a buffered image.
 *
 * @author Luka Čupić
 */
public class ImageRenderer extends G2DRenderer {

	/**
	 * A renderer capable of rendering objects using the {@link Graphics2D}
	 * object.
	 */
	private G2DRenderer renderer;

	/**
	 * The x offset of the image, indicating by how much all objects should be
	 * translated to the left.
	 */
	private int xOffset;

	/**
	 * The y offset of the image, indicating by how much all objects should be
	 * translated upwards.
	 */
	private int yOffset;

	/**
	 * Creates a new instance of this class.
	 *
	 * @param renderer a renderer which handles the rendering on a Graphics2D
	 *                 object
	 * @param xOffset  the x offset of the image
	 * @param yOffset  the y offset of the image
	 */
	public ImageRenderer(G2DRenderer renderer, int xOffset, int yOffset) {
		this.renderer = renderer;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	@Override
	public void render(Point start, Point end, Color color) {
		Point newStart = new Point(start.x - xOffset, start.y - yOffset);
		Point newEnd = new Point(end.x - xOffset, end.y - yOffset);
		renderer.render(newStart, newEnd, color);
	}

	@Override
	public void render(Point center, double radius, Color color) {
		Point newCenter = new Point(center.x - xOffset, center.y - yOffset);
		renderer.render(newCenter, radius, color);
	}

	@Override
	public void render(Point center, double radius, Color color, Color fillColor) {
		Point newCenter = new Point(center.x - xOffset, center.y - yOffset);
		renderer.render(newCenter, radius, color, fillColor);
	}
}
