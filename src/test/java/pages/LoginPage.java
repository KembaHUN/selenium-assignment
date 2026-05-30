package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private static final By USERNAME_INPUT = By.id("username");
    private static final By PASSWORD_INPUT = By.id("password");
    private static final By LOGIN_BUTTON = By.cssSelector("button[type='submit']");
    private static final By ERROR_MESSAGE = By.cssSelector(".error-message");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage navigateToLoginPage(String baseUrl) {
        navigateTo(baseUrl + "login");
        return this;
    }

    public LoginPage enterUsername(String username) {
        sendKeys(USERNAME_INPUT, username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        sendKeys(PASSWORD_INPUT, password);
        return this;
    }

    public LoginPage clickLoginButton() {
        clickElement(LOGIN_BUTTON);
        return this;
    }

    public LoginPage login(String username, String password) {
        enterUsername(username);
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

    public boolean isUsernameInputDisplayed() {
        return isElementDisplayed(USERNAME_INPUT);
    }

    public boolean isPasswordInputDisplayed() {
        return isElementDisplayed(PASSWORD_INPUT);
    }
}