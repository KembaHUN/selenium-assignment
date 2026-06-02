package tests;

import base.BasePage;
import base.BaseTest;
import base.Config;
import java.util.Set;
import org.openqa.selenium.Cookie;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;

public class AuthenticationTest extends BaseTest {

    @Test(priority = 1)
    public void testCookieManipulation() {
        // Navigate to homepage first
        getDriver().get(Config.getBaseUrl());
        
        // Accept cookies via UI if consent dialog is present
        new BasePage(getDriver()).acceptCookieConsent();
        
        // Get initial cookies and print them
        Set<Cookie> initialCookies = getDriver().manage().getCookies();
        System.out.println("Initial cookies count: " + initialCookies.size());
        for (Cookie cookie : initialCookies) {
            System.out.println("Cookie: " + cookie.getName() + " = " + cookie.getValue());
        }
        
        // Add a custom test cookie
        Cookie testCookie = new Cookie("selenium_test_cookie", "test_value");
        getDriver().manage().addCookie(testCookie);
        System.out.println("Added test cookie: selenium_test_cookie");
        
        // Verify cookie was added using BasePage helper
        String cookieValue = getCookieValue("selenium_test_cookie");
        Assert.assertNotNull(cookieValue, "Test cookie should be added");
        Assert.assertEquals(cookieValue, "test_value", "Cookie value should match");
        System.out.println("Verified test cookie exists with value: " + cookieValue);
        
        // Delete the test cookie
        getDriver().manage().deleteCookieNamed("selenium_test_cookie");
        System.out.println("Deleted test cookie: selenium_test_cookie");
        
        // Verify cookie was deleted
        cookieValue = getCookieValue("selenium_test_cookie");
        Assert.assertNull(cookieValue, "Test cookie should be deleted");
        
        System.out.println("Cookie manipulation test passed!");
    }

    @Test(priority = 2, dependsOnMethods = "testCookieManipulation")
    public void testLoginWithValidCredentials() {
        LoginPage loginPage = createPageObject(LoginPage.class);
        
        // Navigate to login page and login, getting HomePage object
        loginPage.navigateToLoginPage(Config.getBaseUrl());
        HomePage homePage = loginPage.loginAndGoToHomePage(Config.getTestEmail(), Config.getTestPassword());
        
        // Verify login was successful using HomePage
        Assert.assertTrue(homePage.isUserLoggedIn(),
            "User should be logged in - user avatar should be displayed");
        
        // Verify page title changed from login page to homepage
        Assert.assertTrue(homePage.isPageTitleMatching("2KDB MyTEAM Database | NBA 2K26"),
            "Page title should match '2KDB MyTEAM Database | NBA 2K26' after successful login");
        
        System.out.println("Login with valid credentials test passed!");
    }

    @Test(priority = 3, dependsOnMethods = "testCookieManipulation")
    public void testLoginWithInvalidCredentials() {
        LoginPage loginPage = createPageObject(LoginPage.class);
        
        // Navigate to login page (handles cookie consent and waits for page load)
        loginPage.navigateToLoginPage(Config.getBaseUrl());
        
        // Perform login with invalid credentials
        loginPage.enterEmail(Config.getInvalidEmail());
        loginPage.enterPassword(Config.getInvalidPassword());
        loginPage.clickLoginButton();
        
        // Verify we remain on the login page (not redirected to homepage)
        Assert.assertTrue(loginPage.isOnLoginPage(),
            "Should remain on login page with invalid credentials");
        
        // Verify error message is displayed
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
            "Error message should be displayed for invalid credentials");
        
        // Verify page title matches login page exactly
        Assert.assertTrue(loginPage.isPageTitleMatching("2KDB MyTEAM Database | Sign In | NBA 2K26"),
            "Page title should match '2KDB MyTEAM Database | Sign In | NBA 2K26' indicating we're still on the login page");
        
        System.out.println("Login with invalid credentials test passed!");
    }

    @Test(priority = 4, dependsOnMethods = "testLoginWithValidCredentials")
    public void testLogout() {
        LoginPage loginPage = createPageObject(LoginPage.class);
        
        // Navigate to login page and login, getting HomePage object
        loginPage.navigateToLoginPage(Config.getBaseUrl());
        HomePage homePage = loginPage.loginAndGoToHomePage(Config.getTestEmail(), Config.getTestPassword());
        
        // Verify we're logged in by checking the home page
        Assert.assertTrue(homePage.isUserLoggedIn(),
            "User should be logged in on the homepage");
        
        // Perform logout - stays on HomePage but user is logged out
        homePage = homePage.logout();
        
        // Verify user is no longer logged in (user button should be gone)
        Assert.assertFalse(homePage.isUserLoggedIn(),
            "User should not be logged in after logout");
        
        // Verify using the isLoggedOut() helper method
        Assert.assertTrue(homePage.isLoggedOut(),
            "isLoggedOut() should return true after logout");
        
        System.out.println("Logout test passed!");
    }

    /**
     * Helper method to get cookie value using BasePage methods.
     */
    private String getCookieValue(String name) {
        Cookie cookie = getDriver().manage().getCookieNamed(name);
        return cookie != null ? cookie.getValue() : null;
    }

}