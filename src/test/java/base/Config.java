package base;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Configuration manager that reads test settings from config.properties.
 * All test configuration values should be accessed through this class.
 */
public class Config {

    private static Properties config;

    static {
        config = new Properties();
        try (FileInputStream fis = new FileInputStream("src/test/resources/config.properties")) {
            config.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    // Application settings
    public static String getBaseUrl() {
        return config.getProperty("app.base_url");
    }

    // Browser settings
    public static String getBrowserName() {
        return config.getProperty("browser.name");
    }

    public static int getWindowWidth() {
        return Integer.parseInt(config.getProperty("browser.window_width", "1920"));
    }

    public static int getWindowHeight() {
        return Integer.parseInt(config.getProperty("browser.window_height", "1080"));
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(config.getProperty("browser.headless", "false"));
    }

    // Timeout settings
    public static int getImplicitWaitSeconds() {
        return Integer.parseInt(config.getProperty("timeout.implicit_wait", "10"));
    }

    public static int getPageLoadTimeoutSeconds() {
        return Integer.parseInt(config.getProperty("timeout.page_load", "30"));
    }

    // Test credentials
    public static String getTestEmail() {
        return config.getProperty("test.email");
    }

    public static String getTestPassword() {
        return config.getProperty("test.password");
    }

    public static String getInvalidEmail() {
        return config.getProperty("invalid.email");
    }

    public static String getInvalidPassword() {
        return config.getProperty("invalid.password");
    }
}