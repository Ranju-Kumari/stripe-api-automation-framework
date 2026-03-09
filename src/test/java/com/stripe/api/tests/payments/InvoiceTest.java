package com.stripe.api.tests.payments;

import com.stripe.api.models.responses.payments.CreateInvoiceResponse;
import com.stripe.api.service.payments.InvoiceService;
import com.stripe.api.tests.base.BaseTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;

public class InvoiceTest extends BaseTest {

    @Test(testName = "Able to create a new invoice for a customer")
    public void createInvoiceTest(){

        String customerId = "cus_TeMQNMrlZet0Xq";

        InvoiceService invoiceService = new InvoiceService();
        Response invoice = invoiceService.createInvoice(customerId);

        invoice.then()
                .statusCode(200)
                .body("customer", equalTo(customerId));

        CreateInvoiceResponse createInvoiceResponse = invoice.as(CreateInvoiceResponse.class);

        Assert.assertEquals(createInvoiceResponse.getCustomer(),customerId);

    }
}
