package com.stripe.utilities;

import io.restassured.http.Cookies;
import io.restassured.response.Response;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import static io.restassured.RestAssured.given;

/**
 * Helper utility to login to Stripe Dashboard via API (REST Assured)
 * and inject session cookies into the WebDriver — bypassing UI login.
 *
 * Flow:
 * 1. POST email to Stripe login endpoint to start the session
 * 2. POST password with the session cookies to complete authentication
 * 3. Inject all session cookies into the browser (WebDriver)
 * 4. Navigate to the customers page so the browser uses the authenticated session
 *
 * The WebDriver must already be on the stripe.com domain before cookies can be set.
 */
public class LoginApiHelper {

    private static final String LOGIN_EMAIL_URL = "https://dashboard.stripe.com/api/account/login/email";
    private static final String LOGIN_PASSWORD_URL = "https://dashboard.stripe.com/api/account/login/password";
    private static final String CUSTOMERS_PAGE_URL = "https://dashboard.stripe.com/test/customers";

    /**
     * Logs in to Stripe Dashboard via API and injects session cookies into the WebDriver.
     * After this method returns, the browser is authenticated and on the customers page.
     *
     * @param driver WebDriver instance (must already be navigated to a stripe.com page)
     */
    public static void loginViaApiAndSetCookies(WebDriver driver) {
        String email = ConfigManager.getKey("ui.username");
        String password = ConfigManager.getKey("ui.password");

        // Step 1: Submit email to get initial session cookies
        Response emailResponse = given()
                .contentType("application/x-www-form-urlencoded")
                .formParam("email", email)
                .when()
                .post(LOGIN_EMAIL_URL);

        System.out.println("Email step status: " + emailResponse.getStatusCode());
        Cookies sessionCookies = emailResponse.getDetailedCookies();

        // Step 2: Submit password with the session cookies from step 1
        Response passwordResponse = given()
                .contentType("application/x-www-form-urlencoded")
                .cookies(sessionCookies)
                .formParam("email", email)
                .formParam("password", password)
                .when()
                .post(LOGIN_PASSWORD_URL);

        System.out.println("Password step status: " + passwordResponse.getStatusCode());

        // Merge cookies from both responses
        Cookies allCookies = passwordResponse.getDetailedCookies();

        // Step 3: Clear existing browser cookies and inject the authenticated ones
        driver.manage().deleteAllCookies();

        injectCookies(driver, sessionCookies);
        injectCookies(driver, allCookies);

        System.out.println("✓ Session cookies injected into WebDriver");

        // Step 4: Navigate to customers page — browser now has authenticated session
        driver.get(CUSTOMERS_PAGE_URL);

        System.out.println("✓ API login complete — navigated to customers page");
    }

    /**
     * Converts Rest Assured cookies to Selenium cookies and adds them to the WebDriver.
     *
     * Special handling for __Host- prefixed cookies:
     * Per the cookie spec, __Host- cookies must NOT have a Domain attribute,
     * must have Secure=true, and must have Path=/. If Domain is set, the browser rejects them.
     */
    private static void injectCookies(WebDriver driver, Cookies cookies) {
        cookies.asList().forEach(restCookie -> {
            try {
                String name = restCookie.getName();
                Cookie.Builder builder = new Cookie.Builder(name, restCookie.getValue())
                        .path(restCookie.getPath() != null ? restCookie.getPath() : "/")
                        .isSecure(true);

                // __Host- cookies MUST NOT have a Domain attribute — browser will reject them otherwise
                if (!name.startsWith("__Host-")) {
                    builder.domain(restCookie.getDomain() != null ? restCookie.getDomain() : ".stripe.com");
                }

                driver.manage().addCookie(builder.build());
            } catch (Exception e) {
                System.err.println("Could not set cookie: " + restCookie.getName() + " — " + e.getMessage());
            }
        });
    }
}

