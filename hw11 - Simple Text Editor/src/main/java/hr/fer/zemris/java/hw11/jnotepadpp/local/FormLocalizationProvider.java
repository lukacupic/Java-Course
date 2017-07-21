package hr.fer.zemris.java.hw11.jnotepadpp.local;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * This class represents a bridge between the main
 * JFrame object and the localization provider.
 * <p>
 * Upon opening the windows of the specified JFrame
 * object, a new connection is established with the
 * provider. After the JFrame window has been closed,
 * the connection is terminated.
 *
 * @author Luka Čupić
 */
public class FormLocalizationProvider extends LocalizationProviderBridge {

    /**
     * Creates a new instance of this class.
     *
     * @param parent the parent provider
     * @param frame  the JFrame object
     */
    public FormLocalizationProvider(ILocalizationProvider parent, JFrame frame) {
        super(parent);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                connect();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                disconnect();
            }
        });
    }
}