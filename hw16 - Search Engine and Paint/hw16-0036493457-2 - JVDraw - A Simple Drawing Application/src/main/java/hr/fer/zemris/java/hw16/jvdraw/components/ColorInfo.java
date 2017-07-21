package hr.fer.zemris.java.hw16.jvdraw.components;

import hr.fer.zemris.java.hw16.jvdraw.components.color.ColorChangeListener;
import hr.fer.zemris.java.hw16.jvdraw.components.color.IColorProvider;

import javax.swing.*;
import java.awt.*;

/**
 * This class represents a small label which changes whenever
 * either the foreground or the background colors have been
 * changed. Needless to say, {@link ColorInfo} class implement
 * the {@link ColorChangeListener}; instances of this class are
 * therefore instantly notified by the  color providers for any
 * changes to the colors.
 *
 * @author Luka Čupić
 */
public class ColorInfo extends JLabel implements ColorChangeListener {

	/**
	 * The provider of the foreground color.
	 */
	private IColorProvider fgColorProvider;

	/**
	 * The provider of the background color.
	 */
	private IColorProvider bgColorProvider;

	/**
	 * Creates a new instance of this class.
	 *
	 * @param fgColorProvider the foreground color provider
	 * @param bgColorProvider the background color provider
	 */
	public ColorInfo(IColorProvider fgColorProvider, IColorProvider bgColorProvider) {
		super("-");

		this.fgColorProvider = fgColorProvider;
		this.bgColorProvider = bgColorProvider;

		fgColorProvider.addColorChangeListener(this);
		bgColorProvider.addColorChangeListener(this);
	}

	@Override
	public void newColorSelected(IColorProvider source, Color oldColor, Color newColor) {
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		Color fgColor = fgColorProvider.getCurrentColor();
		Color bgColor = bgColorProvider.getCurrentColor();

		String text = String.format("Foreground color: (%d, %d, %d), background color: (%d, %d, %d).",
			fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue(),
			bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue()
		);
		super.setText(text);

		super.paintComponent(g);
	}
}
