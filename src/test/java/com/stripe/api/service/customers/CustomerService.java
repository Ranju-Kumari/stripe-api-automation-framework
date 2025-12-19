package com.stripe.api.service.customers;

import com.stripe.api.models.requests.customers.CustomerRequest;
import com.stripe.api.models.requests.customers.UpdateCustomerRequest;
import com.stripe.utilities.ConfigManager;
//import io.restassured.response.Response;

import io.restassured.response.Response;


import java.util.Map;

import static io.restassured.RestAssured.given;


public class CustomerService {

    public Response createCustomer(CustomerRequest request) {

        Response response =
                given()
                        .auth().basic(ConfigManager.getKey("stripe.secret.key"),"")
                        .contentType("application/x-www-form-urlencoded")
                        .formParams(
                                Map.of(
                                        "name", request.getName(),
                                        "email", request.getEmail()
                                )
                        )
                        .post("/customers");


        return response;
    }


    public Response updateCustomer(String customerId, UpdateCustomerRequest request){
        return given()
                .auth().basic(ConfigManager.getKey("stripe.secret.key"),"")
                .contentType("application/x-www-form-urlencoded")
                .formParams("metadata[order_id]", request.getOrderId())
                .post("/customers/"+customerId);
    }

}
