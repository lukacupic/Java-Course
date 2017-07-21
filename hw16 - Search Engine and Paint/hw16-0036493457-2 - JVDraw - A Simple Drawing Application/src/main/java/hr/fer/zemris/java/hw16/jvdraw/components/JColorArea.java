package hr.fer.zemris.java.hw16.jvdraw.components;

import hr.fer.zemris.java.hw16.jvdraw.components.color.ColorChangeListener;
import hr.fer.zemris.java.hw16.jvdraw.components.color.IColorProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides a concrete implementation of a color provider.
 * It represents a small, clickable button which, when clicked, prompts
 * the user to select a new color which will be represented by an instance
 * of this class.
 *
 * @author Luka Čupić
 */
public class JColorArea extends JComponent implements IColorProvider {

	/**
	 * The width of the color button.
	 */
	private static final int WIDTH = 15;

	/**
	 * The height of the color button.
	 */
	private static final int HEIGHT = 15;

	/**
	 * The currently selected color.
	 */
	private Color selectedColor;

	/**
	 * A list of listeners for color changes.
	 */
	private List<ColorChangeListener> listeners = new ArrayList<>();

	/**
	 * Creates a new instance of this class.
	 *
	 * @param color the color to be represented by this provider
	 */
	public JColorArea(Color color) {
		this.selectedColor = color;

		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Color newColor = JColorChooser.showDialog(null, "Choose a color", selectedColor);
				setNewColor((newColor != null) ? newColor : selectedColor);
			}
		});
	}

	/**
	 * Sets the new color for this color provider.
	 *
	 * @param color the new color
	 */
	private void setNewColor(Color color) {
		Color oldColor = selectedColor;
		this.selectedColor = color;

		for (ColorChangeListener l : listeners) {
			l.newColorSelected(this, oldColor, color);
		}
		repaint();
	}

	@Override
	public Color getCurrentColor() {
		return selectedColor;
	}

	@Override
	public void addColorChangeListener(ColorChangeListener l) {
		listeners.add(l);
	}

	@Override
	public void removeColorChangeListener(ColorChangeListener l) {
		listeners.remove(l);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(WIDTH, HEIGHT);
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(selectedColor);
		g.fillRect(0, 0, WIDTH, HEIGHT);
	}
}
