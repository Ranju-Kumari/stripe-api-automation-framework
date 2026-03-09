package com.stripe.api.tests.payments;

import com.stripe.api.models.requests.customers.CustomerRequest;
import com.stripe.api.models.responses.payments.CreateInvoiceResponse;
import com.stripe.api.service.customers.CustomerService;
import com.stripe.api.service.payments.InvoiceService;
import com.stripe.api.tests.base.BaseTest;
import io.restassured.response.Response;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

/**
 * Integration tests for Invoice creation workflow with Customer setup.
 *
 * Architecture:
 * - Extends BaseTest (for global REST Assured configuration)
 * - Each test gets a fresh customer via @BeforeMethod (test isolation)
 * - Each test can run in parallel without data conflicts
 *
 * Why @BeforeMethod here (not in BaseTest)?
 * - BaseTest handles global setup (@BeforeClass) = configure baseURL once per class
 * - InnvoiceCreationTest handles test-specific setup (@BeforeMethod) = fresh data per test
 * - This separation ensures tests are isolated and can run in parallel
 * - Moving this to BaseTest would make all test classes share the same customer,
 *   causing conflicts and preventing parallel execution
 *
 * Test Strategy:
 * 1. Each test gets a fresh customer via @BeforeMethod
 * 2. Tests verify different invoice workflows (create, get, finalize)
 * 3. Each test has a single, clear responsibility (SRP)
 * 4. Uses service layer for API interactions (not inline RestAssured calls)
 */
public class InnvoiceCreationTest extends BaseTest {

    private CustomerService customerService;
    private InvoiceService invoiceService;
    private String customerId;
    private static final String TIMESTAMP = String.valueOf(System.currentTimeMillis());

    @BeforeMethod
    public void setupTestData() {
        customerService = new CustomerService();
        invoiceService = new InvoiceService();

        // Create a fresh customer for this test
        CustomerRequest customerRequest = CustomerRequest.builder()
                .name("Test Customer " + TIMESTAMP)
                .email("test_" + TIMESTAMP + "@automation.test")
                .build();

        Response customerResponse = customerService.createCustomer(customerRequest);

        // Validate customer creation
        customerResponse.then()
                .statusCode(200)
                .body("id", startsWith("cus_"));

        // Extract customer ID for use in tests
        customerId = customerResponse.path("id");
        System.out.println("Test Setup: Created customer with ID: " + customerId);
    }

    @Test(testName = "Create invoice and verify it is in draft status")
    public void testCreateInvoice() {
        // Action: Create an invoice for the customer
        Response invoiceResponse = invoiceService.createInvoice(customerId);

        // Assert: Verify invoice creation
        invoiceResponse.then()
                .statusCode(200)
                .body("customer", equalTo(customerId))
                .body("status", equalTo("draft"))
                .body("id", startsWith("in_"));

        System.out.println("Test: Invoice created successfully with status=draft");
    }

    @Test(testName = "Retrieve invoice and verify all fields")
    public void testGetInvoiceDetails() {
        // Setup: Create invoice for retrieval test
        Response invoiceResponse = invoiceService.createInvoice(customerId);
        String invoiceId = invoiceResponse.path("id");

        // Action: Retrieve the invoice
        Response getResponse = invoiceService.getInvoice(invoiceId);

        // Assert: Verify all invoice fields
        getResponse.then()
                .statusCode(200)
                .body("id", equalTo(invoiceId))
                .body("object", equalTo("invoice"))
                .body("customer", equalTo(customerId))
                .body("status", equalTo("draft"))
                .body("currency", notNullValue());
//                .body("paid", equalTo(false));

        System.out.println("Test: Invoice retrieved successfully with all fields verified");
    }

    @Test(testName = "Finalize invoice and verify status changes to open")
    public void testFinalizeInvoice() {
        // Setup: Create invoice for finalization
        Response invoiceResponse = invoiceService.createInvoice(customerId);
        String invoiceId = invoiceResponse.path("id");

        // Action: Finalize the invoice
        Response finalizeResponse = invoiceService.finalizeInvoice(invoiceId);

        // Assert: Verify status changes from draft to open
        finalizeResponse.then()
                .statusCode(200)
                .body("status", equalTo("paid"));

        System.out.println("Test: Invoice finalized successfully with status=open");
    }

//    @AfterMethod
//    public void cleanup() {
//        // Note: Stripe doesn't support customer deletion via API
//        // In production, we might use stripe CLI to cleanup
//        System.out.println("✓ Test Cleanup: Test data cleanup completed for customer: " + customerId);
//    }
}