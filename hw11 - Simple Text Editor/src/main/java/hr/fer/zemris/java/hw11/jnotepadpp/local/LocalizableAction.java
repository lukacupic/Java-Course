package hr.fer.zemris.java.hw11.jnotepadpp.local;

import javax.swing.*;

/**
 * This class represents an action which is localizable.
 * This means that the contents of the key-value map of
 * this action are changed whenever a change has been
 * made to the localization provider, specified through
 * the class constructor.
 *
 * @author Luka Čupić
 */
public abstract class LocalizableAction extends AbstractAction {

    /**
     * Represents a key for this action.
     */
    private String key;

    /**
     * Represents the localization provider.
     */
    private ILocalizationProvider lp;

    /**
     * Creates a new instance of this class.
     *
     * @param key the key
     * @param lp  the localization provider
     */
    public LocalizableAction(String key, ILocalizationProvider lp) {
        this.key = key;
        this.lp = lp;

        lp.addLocalizationListener(this::updateValues);
    }

    /**
     * A helper method which is called whenever the
     * {@link Action#NAME} and {@link Action#SHORT_DESCRIPTION}
     * values are to be updated.
     */
    private void updateValues() {
        putValue(Action.NAME, lp.getString(key));
        putValue(Action.SHORT_DESCRIPTION, lp.getString(key));
    }
}