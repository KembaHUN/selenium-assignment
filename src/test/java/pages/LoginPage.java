package pages;

import base.BasePage;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page object for the login page (auth/signin).
 * Provides methods for interacting with login form elements and performing login actions.
 * <p>
 * Features:
 * <ul>
 *   <li>Email and password input handling</li>
 *   <li>Login button click with wait for redirect or error</li>
 *   <li>Error message detection for invalid credentials</li>
 *   <li>Fluent API methods for method chaining</li>
 * </ul>
 */
public class LoginPage extends BasePage {

    /**
     * Locator for the email input field.
     */
    private static final By EMAIL_INPUT = By.name("email");
    
    /**
     * Locator for the password input field.
     */
    private static final By PASSWORD_INPUT = By.name("password");
    
    /**
     * Locator for the login submit button.
     */
    private static final By LOGIN_BUTTON = By.cssSelector("button[type='submit']");
    
    /**
     * Locator for the error message container displayed on invalid login.
     */
    private static final By ERROR_MESSAGE = By.cssSelector(".mantine-InputWrapper-error");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Navigates to the login page and accepts cookie consent if present.
     * @param baseUrl the base URL from configuration
     * @return this LoginPage instance for method chaining
     */
    public LoginPage navigateToLoginPage(String baseUrl) {
        navigateTo(baseUrl + "auth/signin");
        acceptCookieConsent();
        waitForLoginPageToLoad();
        return this;
    }

    /**
     * Enters an email address into the email input field.
     * @param email the email address to enter
     * @return this LoginPage instance for method chaining
     */
    public LoginPage enterEmail(String email) {
        sendKeys(EMAIL_INPUT, email);
        return this;
    }

    /**
     * Waits for the login page elements to be visible.
     * Refreshes the page if the email input is not initially found.
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

    /**
     * Enters a password into the password input field.
     * @param password the password to enter
     * @return this LoginPage instance for method chaining
     */
    public LoginPage enterPassword(String password) {
        sendKeys(PASSWORD_INPUT, password);
        return this;
    }

    /**
     * Clicks the login button and waits for either a redirect (valid credentials)
     * or an error message to appear (invalid credentials).
     * @return this LoginPage instance for method chaining
     */
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

    /**
     * Logs in with valid credentials and returns a HomePage object.
     * This method should be used when you want to continue testing authenticated features.
     * @param email the user email
     * @param password the user password
     * @return HomePage object representing the authenticated homepage
     */
    public HomePage loginAndGoToHomePage(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
        // Wait for redirect to homepage
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.not(ExpectedConditions.urlContains("auth/signin")));
        } catch (Exception e) {
            // Continue anyway, we'll verify in the test
        }
        return new HomePage(driver);
    }

    /**
     * Checks if the error message is displayed on the page.
     * @return true if error message is displayed, false otherwise
     */
    public boolean isErrorMessageDisplayed() {
        return isElementDisplayed(ERROR_MESSAGE);
    }

    /**
     * Checks if the email input field is displayed on the page.
     * @return true if email input is displayed, false otherwise
     */
    public boolean isEmailInputDisplayed() {
        return isElementDisplayed(EMAIL_INPUT);
    }

    /**
     * Checks if the password input field is displayed on the page.
     * @return true if password input is displayed, false otherwise
     */
    public boolean isPasswordInputDisplayed() {
        return isElementDisplayed(PASSWORD_INPUT);
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
}