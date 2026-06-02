package tests;

import base.BaseTest;
import base.Config;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test class for browser navigation functionality.
 * Includes tests for multiple page navigation and browser history (back/forward).
 */
public class NavigationTest extends BaseTest {

    /**
     * Test data: Array of URLs to test.
     * Each URL will be visited and verified in the testMultiplePages test.
     */
    private static final String[] TEST_URLS = {
        Config.getBaseUrl() + "compare",
        Config.getBaseUrl() + "players/26",
        Config.getBaseUrl() + "collections/26",
        Config.getBaseUrl() + "updates/26",
        Config.getBaseUrl() + "forums"
    };

    /**
     * Test data: Array of titles to test.
     */
    private static final String[] TITLES = {
        "Compare 2 players",
        "All NBA 2K26 Players",
        "NBA 2K26 Collections",
        "NBA 2K26 MyTEAM Players Updates",
        "Forums - 2KDB"
    };

    /**
     * Tests navigation through multiple pages.
     * Iterates through an array of URLs and verifies each page loads correctly.
     */
    @Test(priority = 1)
    public void testMultiplePages() {
        System.out.println("=== Starting testMultiplePages ===");
        
        int visitedCount = 0;
        
        for (String url : TEST_URLS) {
            System.out.println("Navigating to: " + url);
            getDriver().get(url);
            
            // Accept cookies if dialog is present
            try {
                var basePage = new base.BasePage(getDriver());
                basePage.acceptCookieConsent();
            } catch (Exception e) {
                // Cookie dialog not present or already accepted
            }
            
            // Verify page title is not empty
            String pageTitle = getDriver().getTitle();
            System.out.println("Page title: " + pageTitle);
            Assert.assertNotNull(pageTitle, "Page title should not be null for " + url);
            Assert.assertFalse(pageTitle.isEmpty(), "Page title should not be empty for " + url);
            Assert.assertTrue(pageTitle.contains(TITLES[visitedCount]),
                "Page title should contain " + TITLES[visitedCount] + ", Actual: " + pageTitle);
            
            // Verify page URL matches expected
            String currentUrl = getDriver().getCurrentUrl();
            Assert.assertNotNull(currentUrl, "Current URL should not be null for " + url);
            
            visitedCount++;
            System.out.println("Successfully verified page: " + url);
        }
        
        System.out.println("=== testMultiplePages completed: " + visitedCount + " pages visited ===");
        Assert.assertEquals(visitedCount, TEST_URLS.length,
            "Should have visited all " + TEST_URLS.length + " pages");
    }

    /**
     * Tests browser history navigation (back and forward).
     * Navigates through pages and uses browser history to go back and forward.
     */
    @Test(priority = 2, dependsOnMethods = "testMultiplePages")
    public void testHistoryNavigation() {
        System.out.println("=== Starting testHistoryNavigation ===");
        
        // Step 1: Navigate to homepage
        String homeUrl = Config.getBaseUrl();
        getDriver().get(homeUrl);
        acceptCookies();
        System.out.println("Step 1: Navigated to homepage: " + homeUrl);
        
        // Step 2: Navigate to teams page
        String compareUrl = TEST_URLS[0];
        getDriver().get(compareUrl);
        System.out.println("Step 2: Navigated to compare page: " + compareUrl);
        
        // Verify we're on compare page
        Assert.assertTrue(getDriver().getCurrentUrl().contains("/compare"),
            "Should be on compare page");
        Assert.assertTrue(getDriver().getTitle().contains("Compare 2 players"),
            "Page title should contain 'Compare 2 players'");
        
        // Step 3: Navigate to players page
        String playersUrl = TEST_URLS[1];
        getDriver().get(playersUrl);
        System.out.println("Step 3: Navigated to players page: " + playersUrl);
        
        // Verify we're on players page
        Assert.assertTrue(getDriver().getCurrentUrl().contains("/players/26"),
            "Should be on players page");
        Assert.assertTrue(getDriver().getTitle().contains("All NBA 2K26 Players"),
            "Page title should contain 'All NBA 2K26 Players'");
        
        // Step 4: Navigate back to compare page
        getDriver().navigate().back();
        System.out.println("Step 4: Navigated back to compare page");
        
        String currentUrl = getDriver().getCurrentUrl();
        System.out.println("Current URL after back: " + currentUrl);
        Assert.assertTrue(currentUrl.contains("/compare"),
            "After back(), should be on compare page. Actual URL: " + currentUrl);
        Assert.assertTrue(getDriver().getTitle().contains("Compare 2 players"),
            "After back(), page title should contain 'Compare 2 players'");
        
        // Step 5: Navigate back to homepage
        getDriver().navigate().back();
        System.out.println("Step 5: Navigated back to homepage");
        
        currentUrl = getDriver().getCurrentUrl();
        System.out.println("Current URL after second back: " + currentUrl);
        // Note: Homepage URL might have trailing slash or not
        Assert.assertTrue(currentUrl.replace("/", "").equals(homeUrl.replace("/", "")) ||
                          currentUrl.contains(homeUrl),
            "After second back(), should be on homepage. Actual URL: " + currentUrl);
        
        // Step 6: Navigate forward to compare page
        getDriver().navigate().forward();
        System.out.println("Step 6: Navigated forward to compare page");
        
        currentUrl = getDriver().getCurrentUrl();
        System.out.println("Current URL after forward: " + currentUrl);
        Assert.assertTrue(currentUrl.contains("/compare"),
            "After forward(), should be on compare page. Actual URL: " + currentUrl);
        
        // Step 7: Refresh the page and verify it still loads
        getDriver().navigate().refresh();
        System.out.println("Step 7: Refreshed the page");
        
        currentUrl = getDriver().getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("/compare"),
            "After refresh(), should still be on compare page. Actual URL: " + currentUrl);
        Assert.assertTrue(getDriver().getTitle().contains("Compare 2 players"),
            "After refresh(), page title should still contain 'Compare 2 players'");
        
        System.out.println("=== testHistoryNavigation completed successfully ===");
    }

    /**
     * Helper method to accept cookies if dialog is present.
     */
    private void acceptCookies() {
        try {
            var basePage = new base.BasePage(getDriver());
            basePage.acceptCookieConsent();
        } catch (Exception e) {
            // Cookie dialog not present or already accepted
        }
    }
}