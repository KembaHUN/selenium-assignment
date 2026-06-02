package base;

import static org.openqa.selenium.By.xpath;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Base page object class that provides common WebDriver operations and wait utilities.
 * All page objects should extend this class to gain access to helper methods.
 * <p>
 * Features:
 * <ul>
 *   <li>Explicit wait methods for element visibility, clickability, and presence</li>
 *   <li>JavaScript-based click handling to bypass overlay elements</li>
 *   <li>Cookie consent dialog handling for 2kdb.net</li>
 *   <li>Page navigation and title verification utilities</li>
 * </ul>
 */
public class BasePage {

    /**
     * The WebDriver instance used for all browser operations.
     */
    protected WebDriver driver;
    
    /**
     * WebDriverWait instance with default timeout for explicit waits.
     */
    protected WebDriverWait wait;

    /**
     * Default timeout in seconds for explicit waits.
     */
    protected static final int DEFAULT_TIMEOUT_SECONDS = 10;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));
    }

    /**
     * Waits for an element to be visible on the page.
     * @param locator the By locator to find the element
     * @return the visible WebElement
     */
    protected WebElement waitForElementVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Waits for an element to be clickable on the page.
     * @param locator the By locator to find the element
     * @return the clickable WebElement
     */
    protected WebElement waitForElementClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Waits for an element to be present in the DOM.
     * @param locator the By locator to find the element
     * @return the present WebElement
     */
    protected WebElement waitForElementPresent(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Clicks an element using JavaScript to bypass overlay elements like ads.
     * @param locator the By locator to find the element
     */
    protected void clickElement(By locator) {
        WebElement element = waitForElementClickable(locator);
        // Use JavaScriptExecutor to click, bypassing any overlaying elements like ads
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    /**
     * Sends text input to an element after clearing its current value.
     * @param locator the By locator to find the element
     * @param text the text to send
     */
    protected void sendKeys(By locator, String text) {
        WebElement element = waitForElementVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Checks if an element is displayed on the page.
     * Uses a short timeout (500ms) to avoid long delays when element is not present.
     * @param locator the By locator to find the element
     * @return true if the element is displayed, false otherwise
     */
    protected boolean isElementDisplayed(By locator) {
        try {
            // Use a short timeout to avoid long delays when element is not present
            var shortWait = new WebDriverWait(driver, Duration.ofMillis(500));
            return shortWait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Navigates to the specified URL.
     * @param url the URL to navigate to
     */
    protected void navigateTo(String url) {
        driver.get(url);
    }

    /**
     * Gets the current page title.
     * @return the page title
     */
    protected String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Verifies the page title matches the expected text exactly.
     * @param expectedText the text expected in the title
     * @return true if the title matches the expected text exactly
     */
    public boolean isPageTitleMatching(String expectedText) {
        String title = getPageTitle();
        return title != null && title.equals(expectedText);
    }

    /**
     * Accepts cookie consent dialog if present on 2kdb.net.
     * The cookie dialog has an "Accept" button with onclick="__npcmp('save')".
     */
    public void acceptCookieConsent() {
        try {
            // Wait briefly for the cookie dialog to appear
            var waitShort = new WebDriverWait(driver, Duration.ofSeconds(2));
            var acceptButton = waitShort.until(
                ExpectedConditions.elementToBeClickable(xpath("//button[@onclick=\"__npcmp('save')\"]"))
            );
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", acceptButton);
            System.out.println("Cookie consent accepted using onclick attribute selector.");
        } catch (Exception e) {
            // Cookie dialog not found or already accepted
        }
    }
}
