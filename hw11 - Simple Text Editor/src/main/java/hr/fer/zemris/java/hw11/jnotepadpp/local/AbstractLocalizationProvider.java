package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an abstract localization
 * provider which offers a minimal implementation
 * of the methods inherited from the ILocalizationProvider.
 *
 * @author Luka Čupić
 */
public abstract class AbstractLocalizationProvider implements ILocalizationProvider {

    /**
     * Represents the list of listeners.
     */
    private List<ILocalizationListener> listeners;

    /**
     * Creates a new instance of this class.
     */
    public AbstractLocalizationProvider() {
        listeners = new ArrayList<>();
    }

    /**
     * Called when a change to the localization settings
     * has been made. Invocation of this method will result
     * with notifying all active listeners.
     */
    public void fire() {
        for (ILocalizationListener l : listeners) {
            l.localizationChanged();
        }
    }

    @Override
    public void addLocalizationListener(ILocalizationListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeLocalizationListener(ILocalizationListener listener) {
        listeners.remove(listener);
    }
}
