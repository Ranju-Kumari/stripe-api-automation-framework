package com.stripe.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {

    private static Properties properties;

    static {
            properties = new Properties();
            try (FileInputStream fis = new FileInputStream("src/test/resources/config.properties")) {
                properties.load(fis);
            } catch (Exception e) {
                throw new RuntimeException("Failed to load config", e);
            }

    }

    public static String getKey(String key){
        return properties.getProperty(key);
    }
}