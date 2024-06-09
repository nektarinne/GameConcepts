package com.nektarinne.l18n;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class Settings {

    private final Properties properties = new Properties();
    private final Path settingsPath = Path.of("src", "main", "resources", "settings.properties");

    public Settings() {
        createFileIfNeeded();
        try (InputStream inputStream = Files.newInputStream(settingsPath)) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to read the settings file", e);
        }
    }

    public Object set(String property, String value) {
        Object oldValue = properties.setProperty(property, value);
        saveToFile();
        return oldValue;
    }

    public Object get(String property) {
        return properties.get(property);
    }

    public String getString(String property) {
        return properties.getProperty(property);
    }

    private void saveToFile() {
        try (OutputStream outputStream = Files.newOutputStream(settingsPath)) {
            properties.store(outputStream, "The game settings");
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to save the settings file", e);
        }
    }

    private void createFileIfNeeded() {
        if (Files.notExists(settingsPath)) {
            try {
                Files.createFile(settingsPath);
            } catch (IOException e) {
                throw new UncheckedIOException("Unable to create the settings file", e);
            }
        }
    }

}
