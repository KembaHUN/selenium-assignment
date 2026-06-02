package pages;

import base.BasePage;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage extends BasePage {

    private static final By EMAIL_INPUT = By.name("email");
    private static final By PASSWORD_INPUT = By.name("password");
    private static final By LOGIN_BUTTON = By.cssSelector("button[type='submit']");
    private static final By ERROR_MESSAGE = By.cssSelector(".mantine-InputWrapper-error");
    private static final By USER_BUTTON = By.xpath("//button[contains(@class, 'mantine-Button-root') and .//div[contains(@class, 'mantine-Avatar-root')]]");
    private static final By LOGOUT_BUTTON = By.xpath("//div[contains(@class, 'lucide-log-out')]");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage navigateToLoginPage(String baseUrl) {
        navigateTo(baseUrl + "auth/signin");
        acceptCookieConsent();
        waitForLoginPageToLoad();
        return this;
    }

    public LoginPage enterEmail(String email) {
        sendKeys(EMAIL_INPUT, email);
        return this;
    }

    /**
     * Waits for the login page elements to be visible.
     */
    private void waitForLoginPageToLoad() {
        try {
            waitForElementVisible(EMAIL_INPUT);
        } catch (Exception e) {
            // If email input is not found, refresh and try again
            driver.navigate().refresh();
            waitForElementVisible(EMAIL_INPUT);
        }
    }

    public LoginPage enterPassword(String password) {
        sendKeys(PASSWORD_INPUT, password);
        return this;
    }

    public LoginPage clickLoginButton() {
        clickElement(LOGIN_BUTTON);
        // Wait briefly for either redirect (valid) or error message (invalid)
        try {
            new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.or(
                    // Valid credentials: URL changes
                    ExpectedConditions.not(ExpectedConditions.urlContains("auth/signin")),
                    // Invalid credentials: error message appears
                    ExpectedConditions.visibilityOfElementLocated(ERROR_MESSAGE)
                ));
        } catch (Exception e) {
            // Timeout - continue with assertions
        }
        return this;
    }

    public LoginPage login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
        return this;
    }

    public boolean isErrorMessageDisplayed() {
        return isElementDisplayed(ERROR_MESSAGE);
    }

    public String getErrorMessageText() {
        return getElementText(ERROR_MESSAGE);
    }

    public boolean isEmailInputDisplayed() {
        return isElementDisplayed(EMAIL_INPUT);
    }

    public boolean isPasswordInputDisplayed() {
        return isElementDisplayed(PASSWORD_INPUT);
    }

    /**
     * Checks if the user is logged in by verifying:
     * 1. The page title indicates we're on the homepage (not login page)
     * 2. The user button with avatar is present in the DOM
     * @return true if user is logged in
     */
    public boolean isUserLoggedIn() {
        // First verify we're on the homepage by checking the title
        String title = driver.getTitle();
        if (!title.equals("2KDB MyTEAM Database | NBA 2K26")) {
            // On login page or unknown page
            return false;
        }
        
        try {
            // Look for the user button containing an avatar with a very short timeout
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
     * Checks if we are on the login page by verifying both email and password inputs are displayed.
     * @return true if both email and password inputs are displayed
     */
    public boolean isOnLoginPage() {
        return isEmailInputDisplayed() && isPasswordInputDisplayed();
    }

    /**
     * Gets the current page title.
     * @return the page title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Gets the current page URL.
     * @return the page URL
     */
    public String getPageUrl() {
        return driver.getCurrentUrl();
    }
}