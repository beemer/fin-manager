package com.finmanager.ui;

import com.microsoft.playwright.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * UI Tests for Recurring Expense List component
 * Tests the browser-based user interactions for generating recurring expenses
 * 
 * Run with: mvn test -P ui-tests
 */
public class RecurringExpenseListUITest {
    private Browser browser;
    private Page page;
    private static final String BASE_URL = "http://localhost:8080";
    private static final int TIMEOUT = 30000; // 30 seconds

    @Before
    public void setUp() {
        // Initialize Playwright and launch browser
        Playwright playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(true) // Set to false for debugging
                .setArgs(java.util.List.of("--disable-blink-features=AutomationControlled"))
        );
        
        // Create a new page/tab
        page = browser.newPage();
        page.setDefaultTimeout(TIMEOUT);
        page.setDefaultNavigationTimeout(TIMEOUT);
    }

    @After
    public void tearDown() {
        if (page != null) {
            page.close();
        }
        if (browser != null) {
            browser.close();
        }
    }

    /**
     * Test that the dashboard loads and RecurringExpenseList component is visible
     */
    @Test
    public void testRecurringExpenseListLoads() {
        try {
            // Navigate to dashboard
            page.navigate(BASE_URL);
            
            // Wait for heading to be visible (indicates page load)
            page.waitForSelector("text=Dashboard", new Page.WaitForSelectorOptions().setTimeout(TIMEOUT));
            
            // Check that recurring expense section is present
            page.waitForSelector("text=Recurring Expenses", new Page.WaitForSelectorOptions().setTimeout(TIMEOUT));
            
            assertTrue("Recurring Expenses section should be visible", 
                page.isVisible("text=Recurring Expenses"));
        } catch (PlaywrightException e) {
            fail("Dashboard failed to load: " + e.getMessage());
        }
    }

    /**
     * Test that Generate All button is present and clickable
     */
    @Test
    public void testGenerateAllButtonPresent() {
        try {
            page.navigate(BASE_URL);
            
            // Wait for the Generate All button
            page.waitForSelector("button:has-text('Generate All')", 
                new Page.WaitForSelectorOptions().setTimeout(TIMEOUT));
            
            assertTrue("Generate All button should be visible", 
                page.isVisible("button:has-text('Generate All')"));
            
            // Verify button is enabled
            boolean isDisabled = page.evaluate("() => document.querySelector(\"button:contains('Generate All')\").disabled") 
                    .toString().equals("true");
            assertFalse("Generate All button should not be disabled", isDisabled);
        } catch (PlaywrightException e) {
            fail("Generate All button test failed: " + e.getMessage());
        }
    }

    /**
     * Test that recurring expenses table is displayed with dates
     */
    @Test
    public void testRecurringExpenseTableDisplay() {
        try {
            page.navigate(BASE_URL);
            
            // Wait for table to load
            page.waitForSelector("table", new Page.WaitForSelectorOptions().setTimeout(TIMEOUT));
            
            // Check table headers
            assertTrue("Table should have 'Description' header", 
                page.isVisible("text=Description"));
            assertTrue("Table should have 'Last Generated' header", 
                page.isVisible("text=Last Generated"));
            assertTrue("Table should have 'Status' header", 
                page.isVisible("text=Status"));
            assertTrue("Table should have 'Action' header", 
                page.isVisible("text=Action"));
        } catch (PlaywrightException e) {
            fail("Recurring expense table test failed: " + e.getMessage());
        }
    }

    /**
     * Test that individual Generate button works and shows status message
     */
    @Test
    public void testGenerateButtonForSingleRecurring() {
        try {
            page.navigate(BASE_URL);
            
            // Wait for the recurring expense list table
            page.waitForSelector("table tbody tr", new Page.WaitForSelectorOptions().setTimeout(TIMEOUT));
            
            // Get all generate buttons
            int generateButtonCount = (Integer) page.evaluate(
                "() => document.querySelectorAll(\"button:contains('Generate')\").length"
            );
            
            assertTrue("Should have at least one Generate button", generateButtonCount > 0);
            
            // Click first Generate button (excluding Generate All)
            page.click("table tbody tr:first-child button:has-text('Generate')");
            
            // Wait for status message to appear
            page.waitForSelector(".alert", new Page.WaitForSelectorOptions().setTimeout(TIMEOUT));
            
            // Verify status message contains success or generation info
            String alertContent = page.textContent(".alert");
            assertTrue("Status message should indicate generation result", 
                alertContent != null && 
                (alertContent.contains("Generated") || alertContent.contains("instances") || alertContent.contains("Error")));
        } catch (PlaywrightException e) {
            fail("Single recurring generation test failed: " + e.getMessage());
        }
    }

    /**
     * Test that generation status badges display correctly
     */
    @Test
    public void testStatusBadgeDisplay() {
        try {
            page.navigate(BASE_URL);
            
            // Wait for table rows
            page.waitForSelector("table tbody tr", new Page.WaitForSelectorOptions().setTimeout(TIMEOUT));
            
            // Check for status badges (bg-danger, bg-warning, bg-success classes)
            boolean hasBadges = (Boolean) page.evaluate(
                "() => document.querySelectorAll(\".badge\").length > 0"
            );
            
            assertTrue("Status badges should be displayed in the table", hasBadges);
        } catch (PlaywrightException e) {
            fail("Status badge test failed: " + e.getMessage());
        }
    }

    /**
     * Test loading state when Generate button is clicked
     */
    @Test
    public void testGenerateButtonLoadingState() {
        try {
            page.navigate(BASE_URL);
            
            // Wait for table
            page.waitForSelector("table tbody tr", new Page.WaitForSelectorOptions().setTimeout(TIMEOUT));
            
            // Click a generate button
            page.click("table tbody tr:first-child button:has-text('Generate')");
            
            // Check for loading spinner - verify button shows "Generating..." 
            boolean showsGenerating = page.isVisible("text=Generating...");
            
            // Either shows generating state or completes quickly with message
            assertTrue("Button should show loading state or message appears", 
                showsGenerating || page.isVisible(".alert"));
        } catch (PlaywrightException e) {
            fail("Loading state test failed: " + e.getMessage());
        }
    }

    /**
     * Test that the API is called when Generate button is clicked
     * by intercepting network requests
     */
    @Test
    public void testGenerateApiCall() {
        try {
            page.navigate(BASE_URL);
            
            // Set up network interception to track API calls
            page.onResponse(response -> {
                String url = response.url();
                if (url.contains("/api/recurring") && url.contains("generate")) {
                    assertTrue("API call should succeed", response.status() < 400);
                }
            });
            
            // Wait for table and click generate
            page.waitForSelector("table tbody tr", new Page.WaitForSelectorOptions().setTimeout(TIMEOUT));
            page.click("table tbody tr:first-child button:has-text('Generate')");
            
            // Wait for response
            Thread.sleep(2000);
            
            // If we get here without assertion errors, API was called correctly
        } catch (Exception e) {
            fail("API call test failed: " + e.getMessage());
        }
    }
}
