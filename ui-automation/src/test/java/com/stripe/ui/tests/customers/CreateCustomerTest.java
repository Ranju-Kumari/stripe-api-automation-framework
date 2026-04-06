package com.stripe.ui.tests.customers;

import com.stripe.pageActions.CustomersPageActions;
import com.stripe.ui.base.BaseTest;
import com.stripe.utilities.LoginApiHelper;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CreateCustomerTest extends BaseTest {

    @Test(testName = "Able to navigate to homepage")
    public void navigateToHomePage(){
        CustomersPageActions customersPageActions = new CustomersPageActions(driver);

        customersPageActions.navigateToHomePage();

//      Assert: Verify user has successfully logged in
        Assert.assertTrue(customersPageActions.isUserLoggedIn(),
                         "User should be logged in and navigated to homepage");
    }


    @Test(testName = "Verify able to add customer via API login")
    public void addCustomer(){
        // Login via API — bypasses UI login, injects session cookies into browser
        LoginApiHelper.loginViaApiAndSetCookies(driver);

        // Now add customer via UI (browser is already authenticated)
        CustomersPageActions customersPageActions = new CustomersPageActions(driver);
        customersPageActions.addCustomer();
    }
}
