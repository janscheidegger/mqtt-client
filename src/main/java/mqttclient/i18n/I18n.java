package main.java.mqttclient.i18n;


import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Created by jan on 25/05/15.
 */
public class I18n {

    private static final String BASE_NAME = "i18n.Messages";

    //TODO: Remove Locale German from getBundle
    private static ResourceBundle i18nBundle = ResourceBundle.getBundle(BASE_NAME, Locale.GERMAN);

    public static void setLocale(Locale locale) {
        i18nBundle = ResourceBundle.getBundle(BASE_NAME, locale);
    }

    public static ResourceBundle getResourceBundle() {
        return i18nBundle;
    }

    public static String getString(String key) {
        try {
            return i18nBundle.getString(key);
        } catch (MissingResourceException e) {
            return "Key '" +key +"' not Found";
        }
    }
}
