package hr.fer.zemris.java.hw16.jvdraw.components.color;

import java.awt.*;

/**
 * This class represents a subject in the Visitor design pattern;
 * it notifies all registered listeners about any changes to the
 * (foreground or background) color that user made.
 *
 * @author Luka Čupić
 */
public interface IColorProvider {

	/**
	 * Returns the current color of this provider.
	 *
	 * @return the current color of this provider
	 */
	Color getCurrentColor();

	/**
	 * Adds a color listener to this provider.
	 *
	 * @param l the listener to add
	 */
	void addColorChangeListener(ColorChangeListener l);

	/**
	 * Removes a color listener from this provider.
	 *
	 * @param l the listener to remove
	 */
	void removeColorChangeListener(ColorChangeListener l);
}