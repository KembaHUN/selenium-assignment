package base;

import static org.openqa.selenium.By.xpath;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    protected static final int DEFAULT_TIMEOUT_SECONDS = 10;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));
    }

    protected WebElement waitForElementVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForElementClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected WebElement waitForElementPresent(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    protected void clickElement(By locator) {
        WebElement element = waitForElementClickable(locator);
        // Use JavaScriptExecutor to click, bypassing any overlaying elements like ads
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    protected void sendKeys(By locator, String text) {
        WebElement element = waitForElementVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected boolean isElementDisplayed(By locator) {
        try {
            // Use a short timeout to avoid long delays when element is not present
            var shortWait = new WebDriverWait(driver, Duration.ofMillis(500));
            return shortWait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected void navigateTo(String url) {
        driver.get(url);
    }

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
