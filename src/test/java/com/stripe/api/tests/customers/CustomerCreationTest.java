package com.stripe.api.tests.customers;


import com.stripe.api.models.requests.customers.CustomerRequest;
import com.stripe.api.models.requests.customers.UpdateCustomerRequest;
import com.stripe.api.models.responses.customers.CustomerResponse;
import com.stripe.api.service.customers.CustomerService;
import com.stripe.api.tests.base.BaseTest;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.testng.Assert.*;

public class CustomerCreationTest extends BaseTest {

    CustomerService customerService = new CustomerService();

    @Test(testName = "Able to create a customer successfully")
    public void createCustomer() {

        //Serialization
        CustomerRequest request = CustomerRequest.builder()
                        .name("Jenny Rosen")
                        .email("jennyrosen@example.com")
                        .build();

        Response customer = customerService.createCustomer(request);

        //Response validations
        customer.then()
                .statusCode(200)
                .body("id", startsWith("cus_"))
                .body("name", equalTo(request.getName()))
                .body("email", equalTo(request.getEmail()))
                .body("object", equalTo("customer"));


        //Deserialization
        CustomerResponse customerResponse  = customer.as(CustomerResponse.class);

        // Final assertions
        assertNotNull(customerResponse.getId());
        assertEquals(customerResponse.getName(), "Jenny Rosen");
        assertEquals(customerResponse.getEmail(), "jennyrosen@example.com");
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
}
