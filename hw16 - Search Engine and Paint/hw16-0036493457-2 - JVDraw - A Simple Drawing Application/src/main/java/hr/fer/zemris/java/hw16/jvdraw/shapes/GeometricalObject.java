package hr.fer.zemris.java.hw16.jvdraw.shapes;

import hr.fer.zemris.java.hw16.jvdraw.shapes.rendering.Renderer;

/**
 * This class represents a generic geometric object.
 * All geometric objects which are to be drawn onto
 * the canvas must extend this class and provide an
 * appropriate implementation for that object.
 *
 * @author Luka Čupić
 */
public abstract class GeometricalObject {

	/**
	 * Renders this geometric object onto the canvas.
	 * The purpose of this method is to inform the
	 * renderer about which of the rendering methods
	 * should be used for this particular object.
	 *
	 * @param r the object responsible for rendering
	 *          the object
	 */
	public abstract void render(Renderer r);
}
