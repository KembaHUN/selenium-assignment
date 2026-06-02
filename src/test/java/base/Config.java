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

    /**
     * Gets the base URL of the application under test.
     * @return the base URL
     */
    public static String getBaseUrl() {
        return config.getProperty("app.base_url");
    }

    /**
     * Gets the browser name to use for test execution.
     * @return the browser name (chrome, firefox, or edge)
     */
    public static String getBrowserName() {
        return config.getProperty("browser.name");
    }

    /**
     * Gets the browser window width in pixels.
     * @return the window width (default: 1920)
     */
    public static int getWindowWidth() {
        return Integer.parseInt(config.getProperty("browser.window_width", "1920"));
    }

    /**
     * Gets the browser window height in pixels.
     * @return the window height (default: 1080)
     */
    public static int getWindowHeight() {
        return Integer.parseInt(config.getProperty("browser.window_height", "1080"));
    }

    /**
     * Checks if the browser should run in headless mode.
     * @return true if headless mode is enabled, false otherwise
     */
    public static boolean isHeadless() {
        return Boolean.parseBoolean(config.getProperty("browser.headless", "false"));
    }

    /**
     * Gets the implicit wait timeout in seconds.
     * @return the implicit wait timeout (default: 10)
     */
    public static int getImplicitWaitSeconds() {
        return Integer.parseInt(config.getProperty("timeout.implicit_wait", "10"));
    }

    /**
     * Gets the page load timeout in seconds.
     * @return the page load timeout (default: 30)
     */
    public static int getPageLoadTimeoutSeconds() {
        return Integer.parseInt(config.getProperty("timeout.page_load", "30"));
    }

    /**
     * Gets the valid test email address for login tests.
     * @return the test email address
     */
    public static String getTestEmail() {
        return config.getProperty("test.email");
    }

    /**
     * Gets the valid test password for login tests.
     * @return the test password
     */
    public static String getTestPassword() {
        return config.getProperty("test.password");
    }

    /**
     * Gets an invalid email address for testing error handling.
     * @return an invalid email address
     */
    public static String getInvalidEmail() {
        return config.getProperty("invalid.email");
    }

    /**
     * Gets an invalid password for testing error handling.
     * @return an invalid password
     */
    public static String getInvalidPassword() {
        return config.getProperty("invalid.password");
    }
}