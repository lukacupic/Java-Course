package hr.fer.zemris.java.hw16.jvdraw.files;

import hr.fer.zemris.java.hw16.jvdraw.model.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.model.DrawingModelListener;

/**
 * This class represents a listener of the data model. It registers
 * itself as a listener to the provided {@link DrawingModel} object
 * and it calls the {@link FileActions#setIsSaved} method to reset
 * the saved state of the file.
 *
 * @author Luka Čupić
 */
public class FileChanged implements DrawingModelListener {

	/**
	 * The drawing model.
	 */
	private DrawingModel model;

	public FileChanged(DrawingModel model) {
		this.model = model;
		model.addDrawingModelListener(this);
	}

	@Override
	public void objectsAdded(DrawingModel source, int index0, int index1) {
		fileChanged();
	}

	@Override
	public void objectsRemoved(DrawingModel source, int index0, int index1) {
		fileChanged();
	}

	@Override
	public void objectsChanged(DrawingModel source, int index0, int index1) {
		fileChanged();
	}

	/**
	 * Registers a change to the model and sets the "file saved" flag
	 * to {@code false}.
	 */
	private void fileChanged() {
		FileActions.setIsSaved(false);
	}
}
