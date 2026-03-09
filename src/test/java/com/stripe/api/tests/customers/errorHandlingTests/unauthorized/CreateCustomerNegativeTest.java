package com.stripe.api.tests.customers.errorHandlingTests.unauthorized;

import com.stripe.api.models.requests.customers.CustomerRequest;
import com.stripe.api.service.customers.CustomerService;
import com.stripe.api.tests.base.BaseTest;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.testng.Assert.*;

public class CreateCustomerNegativeTest extends BaseTest {

    @Test(testName = "Verify that when the authorization is invalid, it should give 401 error")
    public void createCustomerInvalidKey() throws Exception {

        CustomerService customerService = new CustomerService();

        CustomerRequest customerRequest = CustomerRequest.builder()
                .name("RK")
                .email("rk@gmail.com")
                .build();

        Response customer = customerService.createCustomerWithInvalidKey(customerRequest);

        customer.then().statusCode(401)
                .body("error.type",equalTo("invalid_request_error"))
                .body("error.message",containsString("Invalid API Key"));


        assertEquals(customer.jsonPath().getString("error.type"), "invalid_request_error");

    }
}
