package com.stripe.api.tests.customers.happyPathTests;


import com.stripe.api.models.requests.customers.CustomerRequest;
import com.stripe.api.models.requests.customers.UpdateCustomerRequest;
import com.stripe.api.models.responses.customers.CustomerResponse;
import com.stripe.api.service.customers.CustomerDBService;
import com.stripe.api.service.customers.CustomerService;
import com.stripe.api.tests.base.BaseTest;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

public class CustomerCreationTest extends BaseTest {

    CustomerService customerService = new CustomerService();
    CustomerDBService customerDBService;

    @BeforeClass
    public void initDBService() {
        customerDBService = new CustomerDBService(dbManager);
    }

    @Test(testName = "Able to create a customer successfully")
    public void createCustomer() {

        //Serialization (Request Serialization)
        CustomerRequest request = CustomerRequest.builder()
                        .name("Jenny Rosen")
                        .email("jennyrosen@example.com")
                        .build();

        Response customer = customerService.createCustomer(request);

        //Response verification
        customer.then()
                .statusCode(200)
                .body("id", startsWith("cus_"))
                .body("name", equalTo(request.getName()))
                .body("email", equalTo(request.getEmail()))
                .body("object", equalTo("customer"));


        //Deserialization  (Response Deserialization)
        CustomerResponse customerResponse  = customer.as(CustomerResponse.class);

        // Final assertions (Validation )
        assertNotNull(customerResponse.getId());
        assertEquals(customerResponse.getName(), "Jenny Rosen");
        assertEquals(customerResponse.getEmail(), "jennyrosen@example.com");

        // Database verification - Ensure customer is persisted in DB
        assertTrue(customerDBService.customerExistsById(customerResponse.getId()),
                "Customer should exist in database after creation");
        assertEquals(customerDBService.getCustomerNameById(customerResponse.getId()),
                request.getName(), "Customer name in DB should match the API request");
        assertEquals(customerDBService.getCustomerEmailById(customerResponse.getId()),
                request.getEmail(), "Customer email in DB should match the API request");
    }


    @Test(testName = "Able to update a customer")
    public void updateCustomer(){

        //First create customer
        //Serialization
        CustomerRequest createRequest = CustomerRequest.builder()
                .name("Jenny Rosen")
                .email("jennyrosen@example.com")
                .build();

        Response customer = customerService.createCustomer(createRequest);

        //Deserialization
        CustomerResponse customerResponse  = customer.as(CustomerResponse.class);

        assertNotNull(customerResponse.getId());

        //Now update the customer
        //Serialization
        UpdateCustomerRequest updateRequest = UpdateCustomerRequest.builder()
                .orderId("123")
                .build();


        Response updateResponse= customerService.updateCustomer(customerResponse.getId(), updateRequest);

        //Response validations

        updateResponse.then()
                .statusCode(200)
                .body("id", startsWith("cus_"))
                .body("metadata.order_id", equalTo(updateRequest.getOrderId()))
                .body("object", equalTo("customer"));

        // Database verification - Ensure metadata is updated in DB
        assertEquals(customerDBService.getCustomerMetadataOrderId(customerResponse.getId()),
                updateRequest.getOrderId(), "Metadata order_id in DB should match the updated value");

        //NOTE:
        /*
        Stripe customers have a fixed schema, but metadata is a mutable key–value map.
        Updating metadata either adds a new key or updates an existing key’s value,
        without changing the customer schema
        */

        /*
        It does not add a new “field” to the customer schema.
        It adds or updates a key inside the customer’s metadata map.
         */

        /*
        Scenario	         Result
        order_id missing	 Key is added
        order_id exists	     Value is updated
        Same value sent	     No change
        Empty value sent	 Key removed
         */

    }

    // Business logic validation
    @Test(testName = "Cannot create duplicate customer with same email")
    public void createDuplicateCustomer() {

        // Create first customer
        CustomerRequest firstRequest = CustomerRequest.builder()
                .name("Jenny Rosen")
                .email("jennyrosen@example.com")
                .build();

        Response firstCustomer = customerService.createCustomer(firstRequest);
        firstCustomer.then()
                .statusCode(200);

        CustomerResponse firstResponse = firstCustomer.as(CustomerResponse.class);
        assertNotNull(firstResponse.getId());

        // Attempt to create duplicate customer with same email
        CustomerRequest duplicateRequest = CustomerRequest.builder()
                .name("Jenny Rosen Duplicate")
                .email("jennyrosen@example.com")
                .build();

        Response duplicateCustomer = customerService.createCustomer(duplicateRequest);

        // Verify conflict response
        duplicateCustomer.then()
                .statusCode(409)
                .body("error.type", equalTo("invalid_request_error"))
                .body("error.code", equalTo("resource_already_exists"))
                .body("error.message", containsString("email"));

        // Database verification - Ensure only the first customer exists in DB
        assertTrue(customerDBService.customerExistsById(firstResponse.getId()),
                "Original customer should still exist in database");
    }

}
