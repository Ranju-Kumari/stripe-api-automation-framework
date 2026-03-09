package com.stripe.api.tests.payments;

import com.stripe.api.service.payments.PaymentIntentService;
import com.stripe.api.tests.base.BaseTest;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;

public class PaymentIntentTest extends BaseTest {

    @Test(testName = "Able to create payment intent successfully")
    public void createPaymentIntent_Success() {

        PaymentIntentService paymentService = new PaymentIntentService();

        Response response = paymentService.createPaymentIntent(50000, "usd");

        // HTTP validation
        response.then()
                .statusCode(200)
                .body("id", startsWith("pi_"))
                .body("amount", equalTo(50000))
                .body("currency", equalTo("usd"))
                .body("object", equalTo("payment_intent"));

        // Additional validation using assertions
        assertEquals(response.jsonPath().getInt("amount"), 5000);
        assertEquals(response.jsonPath().getString("currency"), "usd");
    }
}
