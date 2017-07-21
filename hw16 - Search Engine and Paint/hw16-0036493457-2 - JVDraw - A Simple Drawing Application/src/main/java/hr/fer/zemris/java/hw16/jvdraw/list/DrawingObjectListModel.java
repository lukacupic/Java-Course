package hr.fer.zemris.java.hw16.jvdraw.list;

import hr.fer.zemris.java.hw16.jvdraw.model.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.model.DrawingModelListener;
import hr.fer.zemris.java.hw16.jvdraw.shapes.GeometricalObject;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * This class represents a list model for the JList holding
 * a list of available geometric objects. It also implements
 * the {@link DrawingModelListener}, making it a model listener,
 * so that it can register changes of the drawing model. This
 * class also has a reference to the {@link DrawingModel} itself;
 * all changes to this list model made by the JList are delegated
 * to the drawing model - this makes an instance of this class an
 * adapter of the DrawingModel.
 *
 * @author Luka Čupić
 */
public class DrawingObjectListModel extends AbstractListModel<GeometricalObject> implements DrawingModelListener {

	/**
	 * The drawing model.
	 */
	private DrawingModel model;

	/**
	 * Creates a new instance of this class.
	 *
	 * @param model the drawing model
	 */
	public DrawingObjectListModel(DrawingModel model) {
		this.model = model;
		model.addDrawingModelListener(this);
	}

	@Override
	public int getSize() {
		return model.getSize();
	}

	@Override
	public GeometricalObject getElementAt(int index) {
		return model.getObject(index);
	}

	@Override
	public void objectsAdded(DrawingModel source, int index0, int index1) {
		ListDataEvent e = new ListDataEvent(source, ListDataEvent.INTERVAL_ADDED, index0, index1);

		for (ListDataListener l : getListDataListeners()) {
			l.intervalAdded(e);
		}
	}

	@Override
	public void objectsRemoved(DrawingModel source, int index0, int index1) {
		ListDataEvent e = new ListDataEvent(source, ListDataEvent.INTERVAL_REMOVED, index0, index1);

		for (ListDataListener l : getListDataListeners()) {
			l.intervalRemoved(e);
		}
	}

	@Override
	public void objectsChanged(DrawingModel source, int index0, int index1) {
	}
}