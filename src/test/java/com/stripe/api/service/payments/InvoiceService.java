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
                .formParams("customer", customerId)
                .post("/invoices");
    }

    public Response getInvoice(String invoiceId){
        return given()
                .auth().basic(ConfigManager.getKey("stripe.secret.key"),"")
                .get("/invoices/" + invoiceId);
    }

    public Response finalizeInvoice(String invoiceId){
        return given()
                .auth().basic(ConfigManager.getKey("stripe.secret.key"),"")
                .post("/invoices/" + invoiceId + "/finalize");
    }

    public Response sendInvoice(String invoiceId){
        return given()
                .auth().basic(ConfigManager.getKey("stripe.secret.key"),"")
                .post("/invoices/" + invoiceId + "/send");
    }

    public Response deleteInvoice(String invoiceId){
        return given()
                .auth().basic(ConfigManager.getKey("stripe.secret.key"),"")
                .delete("/invoices/" + invoiceId);
    }
}
