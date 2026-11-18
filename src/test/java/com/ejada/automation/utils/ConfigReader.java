package com.ejada.automation.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static final Properties properties = new Properties();

    static {
        String configPath = "src/test/resources/config/config.properties";
        try (FileInputStream input = new FileInputStream(configPath)) {
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties from: " + configPath, e);
        }
    }

    public static String getProperty(String key) {
        String systemProp = System.getProperty(key);
        if (systemProp != null && !systemProp.isBlank()) {
            return systemProp.trim();
        }
        return properties.getProperty(key).trim();
    }
}