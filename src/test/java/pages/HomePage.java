package pages;

import base.BasePage;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage extends BasePage {

    private static final By USER_BUTTON = By.xpath("//button[@data-with-left-section='true' and .//span[contains(@class, 'mantine-Avatar-placeholder')]]");
    private static final By LOGOUT_BUTTON = By.xpath("//button[@role='menuitem' and contains(., 'Logout')]");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    /**
     * Checks if the user is logged in by verifying the user button is present.
     * @return true if user is logged in
     */
    public boolean isUserLoggedIn() {
        try {
            new WebDriverWait(driver, Duration.ofMillis(500))
                .until(ExpectedConditions.presenceOfElementLocated(USER_BUTTON));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if the logout button is displayed in the user menu.
     * @return true if logout button is displayed
     */
    public boolean isLogoutButtonDisplayed() {
        return isElementDisplayed(LOGOUT_BUTTON);
    }

    /**
     * Clicks the user button to open the user menu.
     * @return this HomePage instance
     */
    public HomePage openUserMenu() {
        WebElement userButton = waitForElementPresent(USER_BUTTON);
        // Scroll into view
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", userButton);
        // Use JavaScript to dispatch a proper click event that React can detect
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].dispatchEvent(new MouseEvent('click', {bubbles: true, cancelable: true}));",
            userButton
        );
        // Wait for the dropdown menu to open using WebDriverWait (dynamic wait)
        new WebDriverWait(driver, Duration.ofSeconds(5))
            .until(ExpectedConditions.visibilityOfElementLocated(LOGOUT_BUTTON));
        return this;
    }

    /**
     * Performs logout by clicking the logout button.
     * The site stays on the homepage but the user is logged out.
     * @return this HomePage instance (now in logged-out state)
     */
    public HomePage logout() {
        // Open user menu if not already open
        if (!isLogoutButtonDisplayed()) {
            openUserMenu();
        }
        
        // Click logout button using MouseEvent dispatch (works with React)
        WebElement logoutButton = waitForElementPresent(LOGOUT_BUTTON);
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].dispatchEvent(new MouseEvent('click', {bubbles: true, cancelable: true}));",
            logoutButton
        );
        
        // Wait for the user button to disappear (indicates logout completed)
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.invisibilityOfElementLocated(USER_BUTTON));
        } catch (Exception e) {
            // Timeout - continue anyway
        }
        
        // Refresh the page to ensure logout state is reflected
        driver.navigate().refresh();
        
        // Return this HomePage instance (now in logged-out state)
        return this;
    }

    /**
     * Verifies that the user has been logged out by checking:
     * The user button (avatar) is no longer present in the DOM.
     * @return true if logged out successfully
     */
    public boolean isLoggedOut() {
        // User should no longer be logged in - user button should be gone
        return !isUserLoggedIn();
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
     * Gets the current page title.
     * @return the page title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }
}