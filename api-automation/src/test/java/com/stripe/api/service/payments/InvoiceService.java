package com.stripe.api.service.payments;

import com.stripe.utilities.ConfigManager;
import io.restassured.response.Response;

import java.util.Map;

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

    /**
     * List all invoices with optional status filter
     * @param status Optional status filter (e.g., "draft", "open", "paid", "uncollectible", "void")
     * @return Response containing list of invoices
     */
    public Response listInvoices(String status){
        return given()
                .auth().basic(ConfigManager.getKey("stripe.secret.key"),"")
                .param("status", status)
                .param("limit", 100)
                .get("/invoices");
    }

    /**
     * List all invoices without filters
     * @return Response containing list of all invoices
     */
    public Response listAllInvoices(){
        return given()
                .auth().basic(ConfigManager.getKey("stripe.secret.key"),"")
                .param("limit", 100)
                .get("/invoices");
    }

    /**
     * List invoices with custom query parameters
     * @param queryParams Map of query parameters (e.g., status, customer, limit)
     * @return Response containing list of invoices
     */
    public Response listInvoicesWithParams(Map<String, String> queryParams){
        return given()
                .auth().basic(ConfigManager.getKey("stripe.secret.key"),"")
                .queryParams(queryParams)
                .get("/invoices");
    }


    public Response getInvoiceRecordsByDraft(String status){
       return  given()
                .auth().basic(ConfigManager.getKey("stripe.secret.key"),"")
               .param("status",status)
               .param("limit",20)
                .get("/invoices");

    }
}
