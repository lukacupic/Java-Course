package hr.fer.zemris.java.hw16.jvdraw.components.color;

import java.awt.*;

/**
 * This class represents a listener for color changes.
 * The listener must register itself on a color provider,
 * which then notifies the listener when a color change
 * occurs.
 *
 * @author Luka Čupić
 */
public interface ColorChangeListener {

	/**
	 * Invoked by a {@link IColorProvider} when a change to the
	 * color has been registered.
	 *
	 * @param source   the provider which triggered the method call
	 * @param oldColor the old color, before the change
	 * @param newColor the new color, after the change
	 */
	void newColorSelected(IColorProvider source, Color oldColor, Color newColor);
}