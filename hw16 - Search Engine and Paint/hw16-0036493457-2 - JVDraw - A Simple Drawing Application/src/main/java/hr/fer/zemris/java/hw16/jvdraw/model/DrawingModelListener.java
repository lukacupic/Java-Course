package hr.fer.zemris.java.hw16.jvdraw.model;

/**
 * This interface represents a listener for changes to the
 * {@link DrawingModel}. Each listener is obligated to register
 * itself on an existing DrawingModel, which will then fire the
 * available methods whenever the drawing model has been modified.
 *
 * @author Luka Čupić
 */
public interface DrawingModelListener {

	/**
	 * Invoked to indicate that an object has been added to the
	 * drawing model.
	 *
	 * @param source the drawing model which invoked the method
	 * @param index0 the lower index value of the interval on
	 *               which the model has been modified
	 * @param index1 the upper index value of the interval on
	 *               which the model has been modified
	 */
	void objectsAdded(DrawingModel source, int index0, int index1);

	/**
	 * Invoked to indicate that an object has been removed from
	 * the drawing model.
	 *
	 * @param source the drawing model which invoked the method
	 * @param index0 the lower index value of the interval on
	 *               which the model has been modified
	 * @param index1 the upper index value of the interval on
	 *               which the model has been modified
	 */
	void objectsRemoved(DrawingModel source, int index0, int index1);

	/**
	 * Invoked to indicate that an object from the drawing model
	 * has been changed.
	 *
	 * @param source the drawing model which invoked the method
	 * @param index0 the lower index value of the interval on
	 *               which the model has been modified
	 * @param index1 the upper index value of the interval on
	 *               which the model has been modified
	 */
	void objectsChanged(DrawingModel source, int index0, int index1);
}