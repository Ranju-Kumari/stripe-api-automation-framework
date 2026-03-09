package com.stripe.api.tests.base;

import com.stripe.utilities.ConfigManager;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

import static io.restassured.RestAssured.baseURI;

/**
 * Base test class for all API tests.
 *
 * Responsibilities:
 * - Global setup: Configure base URL and REST Assured settings (@BeforeClass)
 * - Runs once per test class
 *
 * Note: Test-specific setup (e.g., creating test data) should be in @BeforeMethod
 * in individual test classes to ensure proper test isolation and parallel execution.
 */
public class BaseTest {

    @BeforeClass
    public void setUp() {
        baseURI = ConfigManager.getKey("baseUrl");
//        RestAssured.authentication = RestAssured.basic(ConfigManager.getKey("stripe.secret.key"), "");
        System.out.print("BASE URL :"+ baseURI);
    }
}
 