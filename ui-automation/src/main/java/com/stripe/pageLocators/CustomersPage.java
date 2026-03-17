package com.stripe.pageLocators;

import org.openqa.selenium.By;

public class CustomersPage {

    public static By usernameField = By.cssSelector("[data-testid=\"login-email-input\"]");
    public static By passwordField = By.cssSelector("[data-testid=\"login-password-input\"]");
    public static By signInButton = By.cssSelector("[data-db-analytics-name=\"email_password_input_sign_in_button\"]");
    public static By customersLinkText = By.xpath("//span[contains(text(), 'Customers')]");
}
