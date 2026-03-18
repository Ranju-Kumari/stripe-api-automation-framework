package com.stripe.pageActions;

import com.stripe.utilities.ConfigManager;
import com.stripe.utilities.Waits;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.sql.Time;

import static com.stripe.pageLocators.CustomersPage.*;

public class CustomersPageActions {

    private WebDriver driver;
    private Waits waits;

    public CustomersPageActions(WebDriver driver){
        this.driver = driver;
        this.waits = new Waits(driver);
    }

    public CustomersPageActions navigateToHomePage(){

        WebElement userName = waits.waitForElementToBeClickable(usernameField, 1000);
        userName.sendKeys(ConfigManager.getKey("ui.username"));
        WebElement password = waits.waitForElementToBeClickable(passwordField, 1000);
        password.sendKeys(ConfigManager.getKey("ui.password"));
        WebElement sign = waits.waitForElementToBeClickable(signInButton, 1000);
        sign.click();

        return this;
    }

    /**
     * Verify that user has successfully navigated to homepage
     * Checks if customers link is visible, indicating successful login
     */
    public boolean isUserLoggedIn() {
        try {
            waits.waitForElementToBeVisible(customersLinkText, 10);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public CustomersPageActions addCustomer(){
        WebElement addCustomerPlusBtn = waits.waitForElementToBeClickable(addCustomerButton,10);
        addCustomerPlusBtn.click();
        WebElement name = waits.waitForElementToBeClickable(customerNameInput,1000);
        name.sendKeys("abc");
        WebElement email = waits.waitForElementToBeClickable(customerEmailInput,1000);
        email.sendKeys("abs@gmail.com");
        WebElement addCustomer = waits.waitForElementToBeClickable(addCustomerSubmitButton,1000);
        addCustomer.click();
        return this;
    }


}
