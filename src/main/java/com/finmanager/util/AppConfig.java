package com.finmanager.util;

import java.io.*;
import java.util.Properties;

public class AppConfig {
    private static final String CONFIG_FILE = "application.properties";
    private static AppConfig instance;
    private Properties properties;

    private AppConfig() {
        properties = new Properties();
        loadConfig();
    }

    public static AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }

    private void loadConfig() {
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
        } catch (FileNotFoundException e) {
            loadDefaults();
        } catch (IOException e) {
            e.printStackTrace();
            loadDefaults();
        }
    }

    private void loadDefaults() {
        properties.setProperty("app.name", "fin-manager");
        properties.setProperty("app.version", "1.0.0");
        properties.setProperty("server.port", "8080");
        properties.setProperty("db.path", "fin-manager.db");
        properties.setProperty("data.retention.years", "3");
        properties.setProperty("currency.base", "USD");
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public int getIntProperty(String key, int defaultValue) {
        try {
            return Integer.parseInt(properties.getProperty(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    public void save() {
        try (OutputStream output = new FileOutputStream(CONFIG_FILE)) {
            properties.store(output, "fin-manager Configuration");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
