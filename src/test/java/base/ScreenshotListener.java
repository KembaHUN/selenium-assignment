package base;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG listener that automatically takes screenshots when tests fail.
 * Screenshots are saved to the 'screenshots' folder in the project root.
 */
public class ScreenshotListener implements ITestListener {

    private static final String SCREENSHOT_DIR = "screenshots";

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("Test failed: " + result.getName() + " - Taking screenshot...");
        
        try {
            // Get the WebDriver instance from the test class
            WebDriver driver = getDriverFromTest(result);
            
            if (driver != null) {
                // Take screenshot
                TakesScreenshot ts = (TakesScreenshot) driver;
                File source = ts.getScreenshotAs(OutputType.FILE);
                
                // Generate unique filename with timestamp
                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
                String className = result.getTestClass().getRealClass().getSimpleName();
                String methodName = result.getName();
                String filename = className + "_" + methodName + "_" + timestamp + ".png";
                
                // Ensure screenshots directory exists
                Path screenshotDir = Paths.get(SCREENSHOT_DIR);
                Files.createDirectories(screenshotDir);
                
                // Save screenshot
                Path destination = screenshotDir.resolve(filename);
                Files.copy(source.toPath(), destination);
                
                System.out.println("Screenshot saved: " + destination.toAbsolutePath());
            } else {
                System.out.println("Warning: Could not get WebDriver instance for screenshot");
            }
        } catch (Exception e) {
            System.err.println("Failed to take screenshot: " + e.getMessage());
        }
    }

    /**
     * Extracts the WebDriver instance from the test class.
     * Uses reflection to access the driver field from BaseTest.
     */
    private WebDriver getDriverFromTest(ITestResult result) {
        try {
            Object testInstance = result.getInstance();
            
            // Check if the test class extends BaseTest
            if (testInstance instanceof BaseTest) {
                BaseTest baseTest = (BaseTest) testInstance;
                return baseTest.getDriver();
            }
            
            // Try to get driver via reflection if not directly accessible
            return null;
        } catch (Exception e) {
            System.err.println("Error getting driver from test instance: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        // Not used
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        // Not used
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        // Not used
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // Not used
    }

    @Override
    public void onStart(ITestContext context) {
        // Not used
    }

    @Override
    public void onFinish(ITestContext context) {
        // Not used
    }
}