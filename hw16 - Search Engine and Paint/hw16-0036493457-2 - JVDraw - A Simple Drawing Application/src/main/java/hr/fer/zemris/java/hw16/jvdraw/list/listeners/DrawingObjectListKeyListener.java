package hr.fer.zemris.java.hw16.jvdraw.list.listeners;

import hr.fer.zemris.java.hw16.jvdraw.model.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.shapes.GeometricalObject;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * This class represents a key listener which listens to any
 * changes to the JList made with a keyboard. Specifically,
 * it responds to the press of a DELETE key on an element from
 * the list. As a consequence to being removed from the JList
 * model, the selected element is also removed from the drawing
 * model, which then notifies the canvas to remove the element.
 *
 * @author Luka Čupić
 */
public class DrawingObjectListKeyListener extends KeyAdapter {

	/**
	 * The drawing model.
	 */
	private DrawingModel model;

	/**
	 * Creates a new instance of this class.
	 *
	 * @param model the drawing model
	 */
	public DrawingObjectListKeyListener(DrawingModel model) {
		this.model = model;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_DELETE) {
			JList source = (JList) e.getSource();

			GeometricalObject object = (GeometricalObject) source.getSelectedValue();
			if (object == null) return;

			model.remove(object);
		}
	}
}
