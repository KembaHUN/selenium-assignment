package base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

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
        waitForElementClickable(locator).click();
    }

    protected void sendKeys(By locator, String text) {
        WebElement element = waitForElementVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    protected String getElementText(By locator) {
        return waitForElementVisible(locator).getText();
    }

    protected boolean isElementDisplayed(By locator) {
        try {
            return waitForElementVisible(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean isElementPresent(By locator) {
        try {
            waitForElementPresent(locator);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    protected void navigateTo(String url) {
        driver.get(url);
    }

    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    protected String getPageTitle() {
        return driver.getTitle();
    }
}