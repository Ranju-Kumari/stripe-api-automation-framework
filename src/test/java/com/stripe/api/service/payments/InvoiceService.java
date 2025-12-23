package com.stripe.api.service.payments;

import com.stripe.api.models.requests.payments.CreateInvoiceRequest;
import com.stripe.utilities.ConfigManager;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class InvoiceService {

    public Response createInvoice(String customerId){

        return given()
                .auth().basic(ConfigManager.getKey("stripe.secret.key"),"")
                .contentType("application/x-www-form-urlencoded")
                .formParams("customer",customerId)
                .post("/invoices");
    }
}
