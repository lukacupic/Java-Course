package hr.fer.zemris.java.hw16.jvdraw.list.listeners;

import hr.fer.zemris.java.hw16.jvdraw.list.dialog.DialogUtil;
import hr.fer.zemris.java.hw16.jvdraw.model.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.shapes.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.util.Utility;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This class represents a mouse listener which listens to any
 * interactions with the JList made using a mouse. Specifically,
 * it responds to a double-click on an element from the list
 * and prompts the user with a message dialog that enables
 * the user to make change the properties of the selected object.
 *
 * @author Luka Čupić
 */
public class DrawingObjectListMouseListener extends MouseAdapter {

	/**
	 * The drawing model.
	 */
	private DrawingModel model;

	/**
	 * Creates a new instance of this class.
	 *
	 * @param model the drawing model
	 */
	public DrawingObjectListMouseListener(DrawingModel model) {
		this.model = model;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void mouseClicked(MouseEvent evt) {
		JList<GeometricalObject> list = (JList<GeometricalObject>) evt.getSource();

		if (evt.getClickCount() == 2) {
			int index = list.locationToIndex(evt.getPoint());
			GeometricalObject object = list.getModel().getElementAt(index);

			try {
				DialogUtil.changeObjectProperties(object);
				model.changed(object);

			} catch (IllegalArgumentException ex) {
				Utility.displayError("Illegal input!");
			}
		}
	}
}
