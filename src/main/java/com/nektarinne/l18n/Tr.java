package com.nektarinne.l18n;

import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

public class Tr {

    private static final String LOCALE_SETTING_KEY = "locale";
    private static final String DEFAULT_LANGUAGE_TAG = "en-US";
    private final Settings settings = new Settings();
    private ResourceBundle rb;
    private Locale locale;

    Tr() {
        String tmp = settings.getString(LOCALE_SETTING_KEY);
        if (tmp == null) {
            setLocale(DEFAULT_LANGUAGE_TAG);
        } else {
            locale = Locale.forLanguageTag(tmp);
        }
        rb = ResourceBundle.getBundle("l18n", locale);
    }

    public String setLocale(String newLocale) {
        String old = String.valueOf(locale).replace("_", "-");
        locale = Locale.forLanguageTag(newLocale);
        settings.set(LOCALE_SETTING_KEY, newLocale);
        rb = ResourceBundle.getBundle("l18n", locale);
        return old;
    }

    public String tr(String key, Object... obj) {
        String translatedKey = this.tr(key);
        if (obj == null || obj.length == 0) {
            return translatedKey;
        }
        try {
            return translatedKey.formatted(obj);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("""
                    Unable to format one of the arguments:
                    %s
                    %s""".formatted(translatedKey, Arrays.toString(obj)), e);
        }
    }

    public String tr(String key) {
        if (rb.containsKey(key)) {
            return rb.getString(key);
        }
        return key;
    }
}
