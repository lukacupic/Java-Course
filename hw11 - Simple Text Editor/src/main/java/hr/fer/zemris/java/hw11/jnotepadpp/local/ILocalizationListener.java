package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * This is an interface which represents a localization
 * listener. The listener "listens" to any changes made
 * to the localization settings, and is notified by the
 * invocation of the localizationChanged() method.
 *
 * @author Luka Čupić
 */
public interface ILocalizationListener {

    /**
     * This method is called on any changes made to the
     * localization settings.
     */
    void localizationChanged();
}