package com.stripe.ui.base;

import com.stripe.utilities.ConfigManager;
import io.github.bonigarcia.wdm.managers.ChromeDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * Base UI Test class for all UI automation tests
 *
 * Responsibilities:
 * - Setup: Initialize WebDriver before each test (@BeforeMethod)
 * - Teardown: Close WebDriver after each test (@AfterMethod)
 * - Provides WebDriver instance to all test classes
 *
 * Browser type is read from config.properties (ui.browser)
 * No TestNG parameters needed - configuration is externalized
 */
public abstract class BaseTest {

    protected WebDriver driver;

    @BeforeMethod
    public void setUp() {
        // Get browser type from config (default: chrome)
        String browser = ConfigManager.getKey("ui.browser");
        if (browser == null) {
            browser = "chrome";
        }

        // Initialize WebDriver based on browser type
        if (browser.equalsIgnoreCase("chrome")) {
            ChromeDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        } else if (browser.equalsIgnoreCase("firefox")) {
            // Firefox support - can be added later
            throw new IllegalArgumentException("Firefox not yet configured");
        } else {
            // Default to Chrome
            ChromeDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        }

        // Navigate to base URL
        String baseUrl = ConfigManager.getKey("ui.base.url");
        if (baseUrl != null) {
            driver.get(baseUrl);
        }

        System.out.println("✓ WebDriver initialized with browser: " + browser);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            try {
                driver.quit();
                System.out.println("✓ WebDriver closed successfully");
            } catch (Exception e) {
                System.err.println("Error closing WebDriver: " + e.getMessage());
            }
        }
    }
}
