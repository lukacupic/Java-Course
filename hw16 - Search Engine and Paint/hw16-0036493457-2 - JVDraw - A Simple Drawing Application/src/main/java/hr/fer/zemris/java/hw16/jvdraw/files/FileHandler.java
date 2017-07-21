package hr.fer.zemris.java.hw16.jvdraw.files;

import hr.fer.zemris.java.hw16.jvdraw.shapes.Circle;
import hr.fer.zemris.java.hw16.jvdraw.shapes.FilledCircle;
import hr.fer.zemris.java.hw16.jvdraw.shapes.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.shapes.Line;
import hr.fer.zemris.java.hw16.jvdraw.shapes.Rectangle;
import hr.fer.zemris.java.hw16.jvdraw.shapes.rendering.BoundingRectRenderer;
import hr.fer.zemris.java.hw16.jvdraw.shapes.rendering.G2DRenderer;
import hr.fer.zemris.java.hw16.jvdraw.shapes.rendering.ImageRenderer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class represents a file handler. It offers static methods for
 * file manipulation operations, such as: opening an existing file, saving
 * an existing file and saving a new file.
 *
 * @author Luka Čupić
 */
public class FileHandler {

	/**
	 * A path to the file which is currently being edited.
	 */
	private static Path currentFile = null;

	/**
	 * Prompts the user with a file chooser dialog for selecting a
	 * {@code *.jvd} file, parses the specified file and returns a
	 * list of geometric objects created from the read contents.
	 *
	 * @return a list of geometric objects read from the file, or
	 * {@code null} if the user has cancelled the operation
	 * @throws IOException if an error occurs while reading from the
	 *                     file
	 */
	public static List<GeometricalObject> readJVDFile() throws IOException {
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(FileUtility.getJVDFilter());
		fc.setDialogTitle("Open File");

		if (fc.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
			return null;
		}

		Path path = fc.getSelectedFile().toPath();

		List<String> lines = Files.readAllLines(path);
		currentFile = path;

		return parseLines(lines);
	}

	/**
	 * Parses and returns a list of geometric objects extracted from the given
	 * list of lines.
	 *
	 * @param lines the lines read from the file
	 * @return a list of geometric objects
	 */
	private static List<GeometricalObject> parseLines(List<String> lines) {
		List<GeometricalObject> objects = new ArrayList<>();

		for (String line : lines) {
			String[] parts = line.split(" ");

			String[] subParts = Arrays.copyOfRange(parts, 1, parts.length);
			Integer[] values = FileUtility.toIntArray(subParts);

			if ("LINE".equals(parts[0])) {
				objects.add(createLineObject(values));

			} else if ("CIRCLE".equals(parts[0])) {
				objects.add(createCircleObject(values));

			} else if ("FCIRCLE".equals(parts[0])) {
				objects.add(createFilledCircleObject(values));
			}
		}
		return objects;
	}

	/**
	 * Creates a new {@link Line} object from the given values.
	 *
	 * @param values the list of values representing the line
	 * @return a new {@link Line} object from the given values.
	 */
	private static Line createLineObject(Integer[] values) {
		Point start = new Point(values[0], values[1]);
		Point end = new Point(values[2], values[3]);
		Color color = new Color(values[4], values[5], values[6]);

		return new Line(start, end, color);
	}

	/**
	 * Creates a new {@link Circle} object from the given values.
	 *
	 * @param values the list of values representing the circle
	 * @return a new {@link Circle} object from the given values.
	 */
	private static Circle createCircleObject(Integer[] values) {
		Point center = new Point(values[0], values[1]);
		int radius = values[2];
		Color color = new Color(values[3], values[4], values[5]);

		return new Circle(center, radius, color);
	}

	/**
	 * Creates a new {@link FilledCircle} object from the given values.
	 *
	 * @param values the list of values representing the filled circle
	 * @return a new {@link FilledCircle} object from the given values.
	 */
	private static FilledCircle createFilledCircleObject(Integer[] values) {
		Circle circle = createCircleObject(values);

		Point center = circle.getCenter();
		int radius = (int) circle.getRadius();
		Color color = circle.getColor();
		Color fillColor = new Color(values[6], values[7], values[8]);

		return new FilledCircle(center, radius, color, fillColor);
	}

	/**
	 * Saves the geometric objects by updating the contents of the
	 * currently open file.
	 *
	 * @param objects the list of objects to store to the file
	 * @throws IOException if an error occurs while saving the file
	 */
	public static void saveJVDFile(List<GeometricalObject> objects) throws IOException {
		if (currentFile == null) {
			saveJVDFileAs(objects);
			return;
		}

		List<String> lines = objectsToLines(objects);
		saveFile(lines, currentFile);
	}

	/**
	 * Saves the geometric objects by prompting the user with a file
	 * chooser, and stores the contents into a new file.
	 *
	 * @param objects the list of objects to store to a new file
	 * @throws IOException if an error occurs while saving the file
	 */
	public static void saveJVDFileAs(List<GeometricalObject> objects) throws IOException {
		Path path = getSaveLocation(FileUtility.getJVDFilter(), "Save File");
		if (path == null) return;

		List<String> lines = objectsToLines(objects);
		saveFile(lines, path);
	}

	/**
	 * Converts a given list of geometric objects into their textual
	 * representation.
	 *
	 * @param objects the objects to convert
	 * @return a list of strings, representing the textual representations
	 * of the geometric objects
	 */
	private static List<String> objectsToLines(List<GeometricalObject> objects) {
		List<String> lines = new ArrayList<>();

		for (GeometricalObject object : objects) {

			if (object instanceof Line) {
				lines.add(createLineText((Line) object));

			} else if (object instanceof FilledCircle) {
				lines.add(createFilledCircleText((FilledCircle) object));

			} else if (object instanceof Circle) {
				lines.add(createCircleText((Circle) object));
			}
		}
		return lines;
	}

	/**
	 * Prompts the user with a file dialog which asks for selecting a
	 * location where the file is to be saved.
	 *
	 * @return the path that the user selected as the saving location
	 */
	private static Path getSaveLocation(FileFilter filter, String title) {
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(filter);

		fc.setDialogTitle(title);
		if (fc.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
			return null;
		}

		File file = fc.getSelectedFile();
		Path path = file.toPath();

		if (file.exists()) {
			int pressed = JOptionPane.showConfirmDialog(
				null,
				file.getName() + " already exits. Do you want to replace it?",
				"Replace File?",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE
			);

			if (pressed == JOptionPane.YES_OPTION) {
				return path;
			} else {
				return null;
			}
		} else {
			return path;
		}
	}

	/**
	 * Converts the given {@link Line} object into it's textual
	 * representation.
	 *
	 * @param object the line object to convert
	 * @return a textual representation of the given object
	 */
	private static String createLineText(Line object) {
		Point start = object.getStart();
		Point end = object.getEnd();
		Color color = object.getColor();

		return String.format("%s %d %d %d %d %d %d %d",
			"LINE", start.x, start.y, end.x, end.y,
			color.getRed(), color.getGreen(), color.getBlue()
		);
	}


	/**
	 * Converts the given {@link Circle} object into it's textual
	 * representation.
	 *
	 * @param object the circle object to convert
	 * @return a textual representation of the given object
	 */
	private static String createCircleText(Circle object) {
		Point center = object.getCenter();
		int radius = (int) object.getRadius();
		Color color = object.getColor();

		return String.format("%s %d %d %d %d %d %d",
			"CIRCLE", center.x, center.y, radius,
			color.getRed(), color.getGreen(), color.getBlue()
		);
	}

	/**
	 * Converts the given {@link FilledCircle} object into it's textual
	 * representation.
	 *
	 * @param object the filled circle object to convert
	 * @return a textual representation of the given object
	 */
	private static String createFilledCircleText(FilledCircle object) {
		String text = createCircleText(object);
		text = text.replace("CIRCLE", "FCIRCLE");

		Color color = object.getFillColor();
		return String.format("%s %d %d %d",
			text, color.getRed(), color.getGreen(), color.getBlue()
		);
	}

	/**
	 * Writes the given lines into a file specified by the given path.
	 *
	 * @param lines the list of lines to write
	 * @param path  the path representing the writing location
	 * @throws IOException if an error occurs while writing the lines
	 */
	private static void saveFile(List<String> lines, Path path) throws IOException {
		PrintWriter out = new PrintWriter(path.toString());
		out.write(FileUtility.toString(lines));
		out.close();

		currentFile = path;
	}

	/**
	 * Exports the given list of geometric objects into an image file.
	 *
	 * @param objects the objects to export
	 * @throws IOException if an error occurs while exporting
	 */
	public static void export(List<GeometricalObject> objects) throws IOException {

		BoundingRectRenderer r = new BoundingRectRenderer();
		for (GeometricalObject object : objects) {
			object.render(r);
		}
		Rectangle rect = r.getRect();
		int width = rect.getX2() - rect.getX1();
		int heigth = rect.getY2() - rect.getY1();

		BufferedImage image = new BufferedImage(width, heigth, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g2d = image.createGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, width, heigth);

		ImageRenderer ir = new ImageRenderer(new G2DRenderer(g2d), rect.getX1(), rect.getY1());
		for (GeometricalObject object : objects) {
			object.render(ir);
		}
		g2d.dispose();

		Path path = getSaveLocation(FileUtility.getImageFilter(), "Export");
		if (path == null) return;

		String name = path.toString().toLowerCase();

		String format = "png"; // png by default
		if (name.endsWith(".jpg")) format = "jpg";
		if (name.endsWith(".png")) format = "png";
		if (name.endsWith(".gif")) format = "gif";

		ImageIO.write(image, format, path.toFile());
		JOptionPane.showMessageDialog(null, "Exported!",
			"Success", JOptionPane.INFORMATION_MESSAGE);
	}
}
