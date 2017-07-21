package hr.fer.zemris.java.hw16.jvdraw.components;

import hr.fer.zemris.java.hw16.jvdraw.components.color.ColorChangeListener;
import hr.fer.zemris.java.hw16.jvdraw.components.color.IColorProvider;
import hr.fer.zemris.java.hw16.jvdraw.model.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.model.DrawingModelListener;
import hr.fer.zemris.java.hw16.jvdraw.shapes.Circle;
import hr.fer.zemris.java.hw16.jvdraw.shapes.FilledCircle;
import hr.fer.zemris.java.hw16.jvdraw.shapes.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.shapes.Line;
import hr.fer.zemris.java.hw16.jvdraw.shapes.rendering.G2DRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This class represents the main drawing frame of the JVDraw program.
 * Since this class is a JComponent, it can be added to a layout manager.
 * The JDrawingCanvas object implements a model listener and a change
 * listener. The drawing model provides the information about when the
 * actual model of the geometric objects has been changed, and the color
 * provider provides information about when a color (foreground or background)
 * has been changed, so it can be stored as a new drawing color.
 *
 * @author Luka Čupić
 */
public class JDrawingCanvas extends JComponent implements DrawingModelListener, ColorChangeListener {

	/**
	 * The drawing model, representing the main model of the objects.
	 */
	private DrawingModel model;

	/**
	 * The provider of the foreground color.
	 */
	private IColorProvider fgColorProvider;

	/**
	 * The provider of the background color.
	 */
	private IColorProvider bgColorProvider;

	/**
	 * The Graphics2D renderer, used to draw the objects onto the
	 * canvas.
	 */
	private G2DRenderer renderer = new G2DRenderer();

	/**
	 * Holds the current foreground color (the color from the
	 * foreground color provider).
	 */
	private Color currentFgColor;

	/**
	 * Holds the current background color (the color from the
	 * background color provider).
	 */
	private Color currentBgColor;

	/**
	 * The first point of the object which is currently being
	 * drawn onto the canvas.
	 */
	private Point firstPoint;

	/**
	 * The last point of the object which is currently being
	 * drawn onto the canvas (this value is changing as the
	 * user is dragging the mouse over the canvas, selecting
	 * the final end point).
	 */
	private Point lastPoint;

	/**
	 * Represents the concrete type of the geometric object
	 * currently being drawn onto the canvas.
	 */
	private Class currentObjectType;

	/**
	 * Represents an instance of the geometric object which
	 * is currently being drawn onto the canvas.
	 */
	private GeometricalObject currentObject;

	/**
	 * A flag which registers whether the user has made the
	 * first click on the canvas (i.e. if the user has started
	 * to draw the object).
	 */
	private boolean hasClicked = false;

	/**
	 * Creates a new {@link JDrawingCanvas} object.
	 *
	 * @param model           the geometric objects data model
	 * @param fgColorProvider the foreground color provider
	 * @param bgColorProvider the background color provider
	 */
	public JDrawingCanvas(DrawingModel model, IColorProvider fgColorProvider, IColorProvider bgColorProvider) {
		this.model = model;
		this.fgColorProvider = fgColorProvider;
		this.bgColorProvider = bgColorProvider;

		this.currentFgColor = fgColorProvider.getCurrentColor();
		this.currentBgColor = bgColorProvider.getCurrentColor();

		model.addDrawingModelListener(this);
		fgColorProvider.addColorChangeListener(this);
		bgColorProvider.addColorChangeListener(this);

		addMouseListener(new CanvasMouseListener());
		addMouseMotionListener(new CanvasMouseListener());
	}

	@Override
	protected void paintComponent(Graphics g) {
		renderer.setG2d((Graphics2D) g);

		for (GeometricalObject object : model.getObjects()) {
			object.render(renderer);
		}
	}

	@Override
	public void objectsAdded(DrawingModel source, int index0, int index1) {
		draw();
	}

	@Override
	public void objectsRemoved(DrawingModel source, int index0, int index1) {
		draw();
	}

	@Override
	public void objectsChanged(DrawingModel source, int index0, int index1) {
		draw();
	}

	/**
	 * Redraws the canvas by invoking the {@link #repaint()} method.
	 */
	private void draw() {
		repaint();
	}

	/**
	 * A private class which represents a listener for mouse
	 * movements over the canvas.
	 *
	 * @author Luka Čupić
	 */
	private class CanvasMouseListener extends MouseAdapter {

		@Override
		public void mousePressed(MouseEvent e) {
			if (!hasClicked) {
				firstPoint = e.getPoint();

				String name = currentObjectType.getSimpleName();
				if ("Line".equals(name)) {
					currentObject = new Line(firstPoint, firstPoint, currentFgColor);

				} else if ("Circle".equals(name)) {
					currentObject = new Circle(firstPoint, 0, currentFgColor);

				} else if ("FilledCircle".equals(name)) {
					currentObject = new FilledCircle(firstPoint, 0, currentFgColor, currentBgColor);
				}
				model.add(currentObject);
				hasClicked = true;

			} else {
				hasClicked = false;
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			if (!hasClicked) return;
			lastPoint = e.getPoint();

			if (currentObject instanceof Line) {
				((Line) currentObject).setEnd(lastPoint);

			} else if (currentObject instanceof Circle) {
				((Circle) currentObject).setRadius(firstPoint.distance(lastPoint));
			}
			model.changed(currentObject);
		}
	}

	@Override
	public void newColorSelected(IColorProvider source, Color oldColor, Color newColor) {
		currentFgColor = fgColorProvider.getCurrentColor();
		currentBgColor = bgColorProvider.getCurrentColor();
	}

	/**
	 * Sets the type of the new graphical object to be drawn onto
	 * the canvas.
	 *
	 * @param currentObjectType the type of the new graphical object
	 */
	public void setCurrentObjectType(Class currentObjectType) {
		this.currentObjectType = currentObjectType;
	}
}