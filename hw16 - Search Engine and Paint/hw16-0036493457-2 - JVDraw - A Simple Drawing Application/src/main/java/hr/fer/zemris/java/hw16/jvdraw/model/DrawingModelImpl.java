package hr.fer.zemris.java.hw16.jvdraw.model;

import hr.fer.zemris.java.hw16.jvdraw.shapes.GeometricalObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class provides a concrete implementation of the
 * DrawingModel. See {@link DrawingModel} for more info.
 *
 * @author Luka Čupić
 */
public class DrawingModelImpl implements DrawingModel {

	/**
	 * A list of geometrical objects in this model
	 */
	private List<GeometricalObject> objects = new ArrayList<>();

	/**
	 * A list of listeners to the model.
	 */
	private List<DrawingModelListener> listeners = new ArrayList<>();

	@Override
	public int getSize() {
		return objects.size();
	}

	@Override
	public GeometricalObject getObject(int index) {
		return objects.get(index);
	}

	@Override
	public List<GeometricalObject> getObjects() {
		return Collections.unmodifiableList(objects);
	}

	@Override
	public void add(GeometricalObject object) {
		int index = objects.size();

		objects.add(object);
		for (DrawingModelListener l : listeners) {
			l.objectsAdded(this, index, index);
		}
	}

	@Override
	public void remove(GeometricalObject object) {
		int index = objects.indexOf(object);

		objects.remove(object);
		for (DrawingModelListener l : listeners) {
			l.objectsRemoved(this, index, index);
		}
	}

	@Override
	public void changed(GeometricalObject object) {
		int index = objects.indexOf(object);

		for (DrawingModelListener l : listeners) {
			l.objectsChanged(this, index, index);
		}
	}

	@Override
	public void addDrawingModelListener(DrawingModelListener l) {
		listeners.add(l);
	}

	@Override
	public void removeDrawingModelListener(DrawingModelListener l) {
		listeners.remove(l);
	}
}
