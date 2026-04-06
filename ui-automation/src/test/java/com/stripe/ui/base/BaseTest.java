package com.stripe.ui.base;

import com.stripe.utilities.ConfigManager;
import io.github.bonigarcia.wdm.managers.ChromeDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
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
        String browser = ConfigManager.getInstance().getKey("ui.browser");
        if (browser == null) {
            browser = "chrome";
        }

        // Initialize WebDriver based on browser type
        if (browser.equalsIgnoreCase("chrome")) {
            ChromeDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        } else if (browser.equalsIgnoreCase("firefox")) {
            driver = new FirefoxDriver();

            // Navigate to base URL
            String baseUrl = ConfigManager.getInstance().getKey("ui.base.url");
            if (baseUrl != null) {
                driver.get(baseUrl);
            }
        }
    }

    @AfterMethod
    public void tearDown() {
            if (driver != null) {
                driver.quit();
            }
        }

}
