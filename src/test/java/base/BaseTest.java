package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.lang.reflect.Constructor;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

/**
 * Base test class that provides common WebDriver management and setup/teardown functionality.
 * All test classes should extend this class to gain access to the WebDriver instance
 * and automatic browser initialization.
 * <p>
 * Features:
 * <ul>
 *   <li>Thread-local WebDriver for parallel test execution</li>
 *   <li>Support for Chrome, Firefox, and Edge browsers</li>
 *   <li>Configurable headless mode via config.properties</li>
 *   <li>Automatic driver setup using WebDriverManager</li>
 *   <li>Page object factory method for creating page instances</li>
 * </ul>
 */
public class BaseTest {

    /**
     * Thread-local storage for the WebDriver instance.
     * Ensures each test thread has its own isolated driver instance.
     */
    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    /**
     * Returns the WebDriver instance for the current test thread.
     * @return the WebDriver instance
     */
    protected WebDriver getDriver() {
        return driverThread.get();
    }

    /**
     * Creates a new page object instance of the specified class.
     * Uses reflection to instantiate page objects with the WebDriver constructor argument.
     * @param <T> the page class type extending BasePage
     * @param pageClass the class of the page object to create
     * @return a new instance of the page class
     * @throws RuntimeException if instantiation fails
     */
    protected <T extends BasePage> T createPageObject(Class<T> pageClass) {
        try {
            Constructor<T> constructor = pageClass.getConstructor(WebDriver.class);
            return constructor.newInstance(getDriver());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create page object for " + pageClass.getName(), e);
        }
    }

    /**
     * Sets up the WebDriver before each test method.
     * Initializes the browser based on the browserName parameter or config.properties.
     * Configures browser options including headless mode, window size, and timeouts.
     * @param browserName the name of the browser to use (chrome, firefox, edge)
     */
    @BeforeMethod
    @Parameters({"browser"})
    public void setup(String browserName) {
        // Use parameter from testng.xml if provided, otherwise fall back to config.properties
        if (browserName == null || browserName.isEmpty()) {
            browserName = Config.getBrowserName();
        }
        browserName = browserName.toLowerCase();
        WebDriver driver;
        
        switch (browserName) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("--start-maximized");
                if (Config.isHeadless()) {
                    firefoxOptions.addArguments("--headless");
                }
                driver = new FirefoxDriver(firefoxOptions);
                break;
                
            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--start-maximized");
                edgeOptions.addArguments("--disable-notifications");
                edgeOptions.addArguments("--disable-popup-blocking");
                if (Config.isHeadless()) {
                    edgeOptions.addArguments("--headless");
                }
                driver = new EdgeDriver(edgeOptions);
                break;
                
            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--start-maximized");
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--disable-popup-blocking");
                chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                if (Config.isHeadless()) {
                    chromeOptions.addArguments("--headless=new");
                }
                // Set a realistic user agent to avoid detection
                chromeOptions.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36");
                // Add additional options to mimic real browser behavior
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("profile.default_content_setting_values.notifications", 2);
                chromeOptions.setExperimentalOption("prefs", prefs);
                chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
                driver = new ChromeDriver(chromeOptions);
                break;
        }
        
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Config.getImplicitWaitSeconds()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(Config.getPageLoadTimeoutSeconds()));
        driver.manage().window().setSize(new Dimension(Config.getWindowWidth(), Config.getWindowHeight()));
        
        driverThread.set(driver);
    }

    /**
     * Tears down the WebDriver after each test method.
     * Quits the browser and removes the driver from thread-local storage.
     */
    @AfterMethod
    public void teardown() {
        if (driverThread.get() != null) {
            driverThread.get().quit();
            driverThread.remove();
        }
    }
}