package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This class represents a localization provider.
 * The provider can be accessed through the factory
 * method {@link #getInstance()} which returns an
 * existing instance of the {@link LocalizationProvider}
 * class.
 * <p>
 * Through the instance of this class, the user can
 * obtain the translated phrase by providing the
 * corresponding key.
 *
 * @author Luka Čupić
 */
public class LocalizationProvider extends AbstractLocalizationProvider {

    /**
     * Represents the localization provider which can
     * be obtained through the factory method. This
     * object is the only instance of this class that
     * can ever exist.
     */
    private static final LocalizationProvider instance = new LocalizationProvider();

    /**
     * Represents the current language.
     */
    private String language;

    /**
     * Represents the resource bundle.
     */
    private ResourceBundle bundle;

    /**
     * Don't let anyone instantiate this class.
     */
    private LocalizationProvider() {
        setLanguage("en");
    }

    /**
     * A factory method used to obtain the localization
     * provider.
     *
     * @return a reference to the existing localization
     * provider
     */
    public static LocalizationProvider getInstance() {
        return instance;
    }

    /**
     * Sets a new language, thus making a change to the
     * localization settings.
     *
     * @param language the new language to set
     */
    public final void setLanguage(String language) {
        this.language = language;
        updateBundle();
        fire();
    }

    /**
     * A helper method for updating the bundle.
     */
    private void updateBundle() {
        bundle = ResourceBundle.getBundle(
            "hr.fer.zemris.java.hw11.jnotepadpp.local.prijevodi",
            Locale.forLanguageTag(language)
        );
    }

    /**
     * Gets the current locale.
     *
     * @return the current locale
     */
    public Locale getLocale() {
        return bundle.getLocale();
    }

    @Override
    public String getString(String key) {
        return bundle.getString(key);
    }
}
