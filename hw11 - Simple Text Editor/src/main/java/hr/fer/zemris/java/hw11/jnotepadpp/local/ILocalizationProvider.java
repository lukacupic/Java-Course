package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * This interface represents a localization provider.
 * It's job is to notify all active listeners about
 * the changes made to the localization settings.
 *
 * @author Luka Čupić
 */
public interface ILocalizationProvider {

    /**
     * Adds a specified listener to the list of listeners.
     *
     * @param listener the listener to add
     */
    void addLocalizationListener(ILocalizationListener listener);

    /**
     * Removes the specified listener from the list of listeners.
     *
     * @param listener the listener to remove
     */
    void removeLocalizationListener(ILocalizationListener listener);

    /**
     * Gets the value which the given key maps to.
     *
     * @param key the key
     * @return the translated string which is mapped to
     * by the given key
     */
    String getString(String key);
}
