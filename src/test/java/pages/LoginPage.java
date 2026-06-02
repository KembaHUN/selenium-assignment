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

    public boolean isErrorMessageDisplayed() {
        return isElementDisplayed(ERROR_MESSAGE);
    }

    public boolean isEmailInputDisplayed() {
        return isElementDisplayed(EMAIL_INPUT);
    }

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

    /**
     * Gets the current page URL.
     * @return the page URL
     */
    public String getPageUrl() {
        return driver.getCurrentUrl();
    }
}