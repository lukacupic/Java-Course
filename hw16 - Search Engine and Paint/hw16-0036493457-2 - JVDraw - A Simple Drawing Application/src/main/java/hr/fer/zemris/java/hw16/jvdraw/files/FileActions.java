package hr.fer.zemris.java.hw16.jvdraw.files;

import hr.fer.zemris.java.hw16.jvdraw.model.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.shapes.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.util.Utility;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides access to Action objects which provide
 * some common file manipulation operations, such as: opening
 * an existing file, saving an existing file and saving a new
 * file. Each of the actions expects that an existing Drawing
 * model is available, since each of the actions requires the
 * access to the drawing model of the program. The model must
 * then be set by the caller of these actions during the init
 * of the caller.
 *
 * @author Luka Čupić
 */
public class FileActions {

	/**
	 * The drawing model.
	 */
	private static DrawingModel model;

	/**
	 * A flag which tells whether the current file has been saved
	 * or not, prior to the changes to the {@link DrawingModel}.
	 */
	private static boolean isSaved = true;

	/**
	 * Represents an action for opening an existing document.
	 */
	public static Action openFile = new AbstractAction() {
		{
			this.putValue(Action.NAME, "Open");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				List<GeometricalObject> objects = FileHandler.readJVDFile();
				if (objects == null) return;

				for (GeometricalObject object : new ArrayList<>(model.getObjects())) {
					model.remove(object);
				}

				for (GeometricalObject object : objects) {
					model.add(object);
				}
				isSaved = true;
			} catch (Exception e1) {
				Utility.displayError("An error has occurred while reading from file!");
			}
		}
	};

	/**
	 * Represents an action for saving the currently open
	 * file.
	 */
	public static Action saveFile = new AbstractAction() {
		{
			this.putValue(Action.NAME, "Save");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				FileHandler.saveJVDFile(model.getObjects());
				isSaved = true;
			} catch (IOException e1) {
				Utility.displayError("An error has occurred while saving the file!");
			}
		}
	};

	/**
	 * Represents an action for saving a document under a new name.
	 */
	public static Action saveFileAs = new AbstractAction() {
		{
			this.putValue(Action.NAME, "Save As");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				FileHandler.saveJVDFileAs(model.getObjects());
				isSaved = true;
			} catch (IOException e1) {
				Utility.displayError("An error has occurred while saving the file!");
			}
		}
	};

	/**
	 * Represents an action for exporting the JVD into raster image.
	 */
	public static Action export = new AbstractAction() {
		{
			this.putValue(Action.NAME, "Export");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				FileHandler.export(model.getObjects());
			} catch (IOException ex) {
				Utility.displayError("An error has occurred while exporting the file!");
			}
		}
	};

	/**
	 * Sets the drawing model used by the actions.
	 *
	 * @param dmodel the drawing model
	 */
	public static void setModel(DrawingModel dmodel) {
		model = dmodel;
	}

	/**
	 * Checks if the currently open document has been saved.
	 *
	 * @return true if the document has been saved; false otherwise
	 */
	public static boolean isSaved() {
		return isSaved;
	}

	/**
	 * Sets the "saved status" of the currently open document.
	 *
	 * @param isSaved the new falue of the flag
	 */
	public static void setIsSaved(boolean isSaved) {
		FileActions.isSaved = isSaved;
	}
}
