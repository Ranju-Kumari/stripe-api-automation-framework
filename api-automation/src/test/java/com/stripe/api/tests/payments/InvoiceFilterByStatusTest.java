package com.stripe.api.tests.payments;

import com.stripe.api.models.responses.payments.InvoiceData;
import com.stripe.api.models.responses.payments.ListInvoicesResponse;
import com.stripe.api.service.payments.InvoiceService;
import com.stripe.api.tests.base.BaseTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

/**
 * Test class for filtering invoices by status.
 * Demonstrates automation for viewing invoices with specific status (e.g., draft)
 */
public class InvoiceFilterByStatusTest extends BaseTest {

    @Test(testName = "Filter invoices by draft status")
    public void filterInvoicesByDraftStatusTest(){
        InvoiceService invoiceService = new InvoiceService();

        // Call API to list all invoices with status = "draft"
        Response response = invoiceService.listInvoices("draft");

        // Validate the response
        response.then()
                .statusCode(200)
                .body("object", equalTo("list"))
                .body("data", notNullValue());

        // Parse the response to ListInvoicesResponse
        ListInvoicesResponse invoicesResponse = response.as(ListInvoicesResponse.class);

        // Validate that the response has data
        Assert.assertNotNull(invoicesResponse.getData(), "Invoice data should not be null");

        // Validate that all invoices have status = "draft"
        List<InvoiceData> invoicesResponseList = invoicesResponse.getData();

        invoicesResponseList.forEach(invoice -> {
            assertThat(invoice.getStatus())
                    .as("All invoices should have status 'draft'")
                    .isEqualTo("draft");
            assertThat(invoice.getId())
                    .as("Invoice ID should not be null")
                    .isNotNull();
            assertThat(invoice.getCustomer())
                    .as("Invoice customer should not be null")
                    .isNotNull();

            assertEquals(invoice.getStatus(),"draft");
        });
    }

    @Test(testName = "Filter invoices by status - open")
    public void filterInvoicesByOpenStatusTest(){

        InvoiceService invoiceService = new InvoiceService();
        Response response = invoiceService.listInvoices("open");

        response.then()
                .statusCode(200)
                .body("object", equalTo("list"));

        ListInvoicesResponse invoicesResponse = response.as(ListInvoicesResponse.class);

        // Validate all invoices have status = "open"
        invoicesResponse.getData().forEach(invoice -> {
            assertThat(invoice.getStatus())
                    .as("All invoices should have status 'open'")
                    .isEqualTo("open");
        });
    }

    @Test(testName = "List all invoices without status filter")
    public void listAllInvoicesTest(){

        InvoiceService invoiceService = new InvoiceService();
        Response response = invoiceService.listAllInvoices();

        response.then()
                .statusCode(200)
                .body("object", equalTo("list"));

        ListInvoicesResponse invoicesResponse = response.as(ListInvoicesResponse.class);

        if(invoicesResponse.getData() != null) {
            System.out.println("Total invoices found: " + invoicesResponse.getData().size());
        } else {
            System.out.println("No invoices found - data is null");
        }
    }

    @Test(testName = "Filter invoices by draft status with limit")
    public void filterInvoicesByStatusWithCustomParamsTest(){

        InvoiceService invoiceService = new InvoiceService();

        // Create query parameters map
        java.util.Map<String, String> queryParams = new java.util.HashMap<>();
        queryParams.put("status", "draft");
        queryParams.put("limit", "50");

        Response response = invoiceService.listInvoicesWithParams(queryParams);

        response.then()
                .statusCode(200)
                .body("object", equalTo("list"));

        ListInvoicesResponse invoicesResponse = response.as(ListInvoicesResponse.class);

        // Validate results
        if(invoicesResponse.getData() != null) {
            assertThat(invoicesResponse.getData().size())
                    .as("Result should be limited to 50")
                    .isLessThanOrEqualTo(50);

            invoicesResponse.getData().forEach(invoice -> {
                assertThat(invoice.getStatus())
                        .as("All invoices should have status 'draft'")
                        .isEqualTo("draft");
            });

            System.out.println("Draft invoices found (limit 50): " + invoicesResponse.getData().size());
        } else {
            System.out.println("No draft invoices found - data is null");
        }
    }

}

