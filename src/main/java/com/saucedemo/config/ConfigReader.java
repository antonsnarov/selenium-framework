package com.saucedemo.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Читает настройки из config.properties.
 * Singleton — создаётся один раз на всё время запуска тестов.
 */
public class ConfigReader {

    private static ConfigReader instance;
    private final Properties properties = new Properties();

    private ConfigReader() {
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("config.properties not found in resources");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }

    public String get(String key) {
        // System property имеет приоритет — удобно для переопределения из CI
        String sysProp = System.getProperty(key);
        return sysProp != null ? sysProp : properties.getProperty(key);
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    public int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    // Shortcuts для часто используемых настроек
    public String getBrowser()      { return get("browser"); }
    public String getBaseUrl()      { return get("base.url"); }
    public String getGridUrl()      { return get("grid.url"); }
    public boolean isHeadless()     { return getBoolean("headless"); }
    public boolean isRemote()       { return getBoolean("remote"); }
    public int getImplicitWait()    { return getInt("implicit.wait"); }
    public int getExplicitWait()    { return getInt("explicit.wait"); }
    public int getPageLoadTimeout() { return getInt("page.load.timeout"); }
    public String getPassword()     { return get("password"); }
    public String getStandardUser() { return get("user.standard"); }
    public String getLockedUser()   { return get("user.locked"); }
}
