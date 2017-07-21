package hr.fer.zemris.java.hw11.jnotepadpp.local;

import javax.swing.*;

/**
 * Represents a localized JMenu object.
 * All changes to the current language will trigger
 * this object and cause it to change it's text to
 * the corresponding translation, i.e. the one which
 * is specified by the current locale.
 *
 * @author Luka Čupić
 */
public class LJMenu extends JMenu {

    /**
     * Represents the key which maps to the value
     * of this menu.
     */
    private String key;

    /**
     * Creates a new instance of this class.
     *
     * @param key the key
     * @param lp the localization provider
     */
    public LJMenu(String key, ILocalizationProvider lp) {
        this.key = key;

        setText(lp.getString(key));

        lp.addLocalizationListener(() -> {
            this.key = key;
            setText(lp.getString(key));
        });
    }
}
