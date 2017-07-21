package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * This class represents a bridge between the localization
 * listener and the provider of the localization changes.
 *
 * @author Luka Čupić
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider {

    /**
     * Represents a provider to which all the requests
     * are delegated to.
     */
    private ILocalizationProvider parent;

    /**
     * Represents a listener for localization changes,
     * which then notifies all the remaining listeners.
     */
    private ILocalizationListener listener;

    /**
     * Represents a flag which denotes whether or not the connection
     * is currently present.
     */
    private boolean connected;

    /**
     * Creates a new instance of this class.
     *
     * @param parent the parent to which all the
     *               requests are delegated to
     */
    public LocalizationProviderBridge(ILocalizationProvider parent) {
        this.parent = parent;
        this.listener = this::fire;
        connected = false;
    }

    /**
     * Connects the localization listener to the localization
     * provider, i.e. "connects the bridge".
     */
    public void connect() {
        if (connected) return;
        parent.addLocalizationListener(listener);
    }

    /**
     * Disconnects the localization listener from the localization
     * provider, i.e. "disconnects the bridge".
     */
    public void disconnect() {
        if (!connected) return;
        parent.removeLocalizationListener(listener);
    }

    @Override
    public String getString(String key) {
        return parent.getString(key);
    }
}
