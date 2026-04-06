package com.stripe.api.tests.base;

import com.stripe.utilities.ConfigManager;
import com.stripe.utilities.DatabaseManager;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import static io.restassured.RestAssured.baseURI;

/**
 * Base test class for all API tests.
 *
 * Responsibilities:
 * - Global setup: Configure base URL and REST Assured settings (@BeforeClass)
 * - Database connection lifecycle management
 * - Runs once per test class
 *
 * Note: Test-specific setup (e.g., creating test data) should be in @BeforeMethod
 * in individual test classes to ensure proper test isolation and parallel execution.
 */
public class BaseTest {

    protected DatabaseManager dbManager;

    @BeforeClass
    public void setUp() {
        baseURI = ConfigManager.getKey("baseUrl");

        // Database setup
        dbManager = new DatabaseManager();
        dbManager.connect();
    }

    @AfterClass
    public void tearDown() {
        if (dbManager != null) {
            dbManager.close();
        }
    }
}
 