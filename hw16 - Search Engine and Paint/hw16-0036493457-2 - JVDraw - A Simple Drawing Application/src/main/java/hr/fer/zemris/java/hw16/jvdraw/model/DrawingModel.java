package hr.fer.zemris.java.hw16.jvdraw.model;

import hr.fer.zemris.java.hw16.jvdraw.shapes.GeometricalObject;

import javax.swing.*;
import java.util.List;

/**
 * This interface represents the main model for drawing objects
 * onto the JDrawingCanvas. It provides an API towards all of
 * the geometric objects currently existing on the canvas. All
 * interested listeners must register itself on an instance of
 * this class, to be able to receive notifications about changes
 * in the drawing model (i.e. geometric objects themselves).
 *
 * @author Luka Čupić
 */
public interface DrawingModel {

	/**
	 * Returns the number of geometric elements currently available
	 * in the model.
	 *
	 * @return the number of geometric elements in the model
	 */
	int getSize();

	/**
	 * Fetches a geometric object at the specified index in the collection.
	 *
	 * @param index the index of the geometric object to fetch
	 * @return a geometric object at a certain index in the collection
	 */
	GeometricalObject getObject(int index);

	/**
	 * Provides a read-only view of all the geometric objects available
	 * in the model.
	 *
	 * @return a an immutable list of all geometric objects in the model.
	 */
	List<GeometricalObject> getObjects();

	/**
	 * Adds a new geometric object into the model.
	 *
	 * @param object the geometric object to be added to the model
	 */
	void add(GeometricalObject object);

	/**
	 * Removes an existing geometric object from the model.
	 *
	 * @param object the geometric object to be removed from the model
	 */
	void remove(GeometricalObject object);

	/**
	 * Called when a change to an object from the model has
	 * been made, so that the model can register the change.
	 *
	 * @param object the changed object
	 */
	void changed(GeometricalObject object);

	/**
	 * Adds a new model listener.
	 *
	 * @param l the listener to be added
	 */
	void addDrawingModelListener(DrawingModelListener l);


	/**
	 * Removes an existing model listener.
	 *
	 * @param l the listener to be removed
	 */
	void removeDrawingModelListener(DrawingModelListener l);
}