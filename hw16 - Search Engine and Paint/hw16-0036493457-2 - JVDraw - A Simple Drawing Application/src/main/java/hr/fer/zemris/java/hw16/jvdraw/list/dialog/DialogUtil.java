package hr.fer.zemris.java.hw16.jvdraw.list.dialog;

import hr.fer.zemris.java.hw16.jvdraw.shapes.Circle;
import hr.fer.zemris.java.hw16.jvdraw.shapes.FilledCircle;
import hr.fer.zemris.java.hw16.jvdraw.shapes.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.shapes.Line;

import javax.swing.*;
import java.awt.*;

/**
 * This class is a utility class for the dialog windows shown
 * to the user when changing an element from the list of objects.
 *
 * @author Luka Čupić
 */
public class DialogUtil {

	//region Dialog Extraction

	/**
	 * Changes the properties of the specified geometric object.
	 *
	 * @param object the object to change
	 */
	public static void changeObjectProperties(GeometricalObject object) {
		JPanel editor;
		if (object instanceof Line) {
			editor = DialogUtil.createLineEditor(((Line) object));
			showDialog(editor, object);

			changeLineProperties(editor, (Line) object);

		} else if (object instanceof FilledCircle) {
			editor = DialogUtil.createFilledCircleEditor(((FilledCircle) object));
			showDialog(editor, object);

			changeFilledCircleProperties(editor, (FilledCircle) object);

		} else if (object instanceof Circle) {
			editor = DialogUtil.createCircleEditor((Circle) object);
			showDialog(editor, object);

			changeCircleProperties(editor, (Circle) object);
		}
	}

	/**
	 * Changes all properties of the given line; the new properties
	 * are extracted from the provided JPanel.
	 *
	 * @param editor the editor from which to extract values from
	 * @param line   the line to update
	 */
	private static void changeLineProperties(JPanel editor, Line line) {
		line.setStart(getPoint(editor, 0));
		line.setEnd(getPoint(editor, 1));
		line.setColor(getColor(editor, 2));
	}

	/**
	 * Changes all properties of the given circle; the new
	 * properties are extracted from the provided JPanel.
	 *
	 * @param editor the editor from which to extract values from
	 * @param circle the circle to update
	 */
	private static void changeCircleProperties(JPanel editor, Circle circle) {
		circle.setCenter(getPoint(editor, 0));
		circle.setRadius(getValues(editor, 1, 1)[0]);
		circle.setColor(getColor(editor, 2));
	}

	/**
	 * Changes all properties of the given filled circle; the new
	 * properties are extracted from the provided JPanel.
	 *
	 * @param editor the editor from which to extract values from
	 * @param circle the filled circle to update
	 */
	private static void changeFilledCircleProperties(JPanel editor, FilledCircle circle) {
		changeCircleProperties(editor, circle);
		circle.setFillColor(getColor(editor, 3));
	}

	/**
	 * Extracts the information about a point from the provided
	 * JPanel and returns it as a new Point object.
	 *
	 * @param editor the editor from which to extract values from
	 * @param row    the row in which the values are contained
	 * @return a new point extracted from the provided editor
	 */
	private static Point getPoint(JPanel editor, int row) {
		int values[] = getValues(editor, row, 2);
		return new Point(values[0], values[1]);
	}

	/**
	 * Extracts the information about a color from the provided
	 * JPanel and returns it as a new Color object.
	 *
	 * @param editor the editor from which to extract values from
	 * @param row    the row in which the values are contained
	 * @return a new color extracted from the provided editor
	 */
	private static Color getColor(JPanel editor, int row) {
		int values[] = getValues(editor, row, 3);
		return new Color(values[0], values[1], values[2]);
	}

	/**
	 * Extracts the information from the provided JPanel and
	 * returns it as a new array of int values.
	 *
	 * @param editor the editor from which to extract values from
	 * @param row    the row in which the values are contained
	 * @return a new color extracted from the provided editor
	 */
	private static int[] getValues(JPanel editor, int row, int noOfFields) {
		JPanel panel = ((JPanel) editor.getComponents()[row]);

		int[] values = new int[noOfFields];
		for (int i = 1; i <= noOfFields; i++) {
			values[i - 1] = getFieldValue(panel, i);
		}
		return values;
	}

	/**
	 * Extracts an information from the provided JPanel at
	 * the specified index.
	 *
	 * @param panel the panel from which to extract value from
	 * @param index the index of the JTextField component
	 * @return a new color extracted from the provided editor
	 */
	private static int getFieldValue(JPanel panel, int index) {
		return Integer.parseInt(((JTextField) panel.getComponents()[index]).getText());
	}
	//endregion

	//region Dialog Creation

	/**
	 * Creates a new editor, which displays the interface for editing
	 * a {@link Line} geometric object.
	 *
	 * @param object the object to edit via this editor
	 * @return a JPanel object, representing the editor for the provided
	 * object
	 */
	public static JPanel createLineEditor(Line object) {
		JPanel editor = new JPanel(new GridLayout(3, 1));

		editor.add(createEditorRow("Start (x, y):", new Object[]{
			object.getStart().x, object.getStart().y
		}));

		editor.add(createEditorRow("End (x, y):", new Object[]{
			object.getEnd().x, object.getEnd().y
		}));

		Color color = object.getColor();
		editor.add(createEditorRow("Color:", new Object[]{
			color.getRed(), color.getGreen(), color.getBlue()
		}));

		return editor;
	}

	/**
	 * Creates a new editor, which displays the interface for editing
	 * a {@link Circle} geometric object.
	 *
	 * @param object the object to edit via this editor
	 * @return a JPanel object, representing the editor for the provided
	 * object
	 */
	public static JPanel createCircleEditor(Circle object) {
		JPanel editor = new JPanel(new GridLayout(3, 1));

		createRawCircleEditor(editor, object);

		return editor;
	}

	/**
	 * Creates a new editor, which displays the interface for editing
	 * a {@link FilledCircle} geometric object.
	 *
	 * @param object the object to edit via this editor
	 * @return a JPanel object, representing the editor for the provided
	 * object
	 */
	public static JPanel createFilledCircleEditor(FilledCircle object) {
		JPanel editor = new JPanel(new GridLayout(4, 1));

		createRawCircleEditor(editor, object);

		Color color = object.getColor();
		editor.add(createEditorRow("Fill Color:", new Object[]{
			color.getRed(), color.getGreen(), color.getBlue()
		}));

		return editor;
	}

	/**
	 * Creates the basic circle editor, used by both {@link #createCircleEditor}
	 * and {@link #createFilledCircleEditor}.
	 *
	 * @param editor the editor to append the components to
	 * @param object the object to edit via this editor
	 */
	private static void createRawCircleEditor(JPanel editor, Circle object) {
		editor.add(createEditorRow("Center (x, y):", new Object[]{
			object.getCenter().x, object.getCenter().y
		}));

		editor.add(createEditorRow("Radius:", new Object[]{
			(int) object.getRadius()
		}));

		Color color = object.getColor();
		editor.add(createEditorRow("Outline Color:", new Object[]{
			color.getRed(), color.getGreen(), color.getBlue()
		}));
	}

	/**
	 * Creates a new row which will be stored in the editor. Each row
	 * has a name and a set of values, which will be stored in the
	 * JTextField objects.
	 *
	 * @param name   the name of the row (that will be displayed at the
	 *               beginning of the row)
	 * @param values an array of existing values to place into the
	 *               JTextField objects
	 * @return a new JPanel object, representing a single row of the
	 * editor
	 */
	private static JPanel createEditorRow(String name, Object[] values) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.add(new JLabel(name));

		for (Object value : values) {
			JTextField field = new JTextField(value.toString());
			field.setPreferredSize(new Dimension(30, 20));
			panel.add(field);
		}
		return panel;
	}
	//endregion

	/**
	 * Shows the message dialog, prompting the user to enter
	 * the properties of the object which are to be changed.
	 *
	 * @param editor the editor object, encapsulating all
	 *               relevant fields for the user interaction
	 * @param object the object which is to be changed
	 */
	private static void showDialog(JPanel editor, GeometricalObject object) {
		JOptionPane.showMessageDialog(null, editor, object.toString(), JOptionPane.PLAIN_MESSAGE);
	}
}
