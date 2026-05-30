package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Constructor;
import java.time.Duration;

public class BaseTest {

    protected static final String BASE_URL = "https://2kdb.net/";
    protected static final int IMPLICIT_WAIT_SECONDS = 10;
    protected static final int PAGE_LOAD_TIMEOUT_SECONDS = 30;

    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    protected WebDriver getDriver() {
        return driverThread.get();
    }

    protected <T extends BasePage> T createPageObject(Class<T> pageClass) {
        try {
            Constructor<T> constructor = pageClass.getConstructor(WebDriver.class);
            return constructor.newInstance(getDriver());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create page object for " + pageClass.getName(), e);
        }
    }

    @BeforeMethod
    public void setup() {
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        
        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_WAIT_SECONDS));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(PAGE_LOAD_TIMEOUT_SECONDS));
        driver.manage().window().maximize();
        
        driverThread.set(driver);
    }

    @AfterMethod
    public void teardown() {
        if (driverThread.get() != null) {
            driverThread.get().quit();
            driverThread.remove();
        }
    }
}