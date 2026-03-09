package com.stripe.api.service.payments;

import com.stripe.utilities.ConfigManager;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class PaymentIntentService {

    public Response createPaymentIntent(int amount, String currency) {

        return given()
                .auth().basic(ConfigManager.getKey("stripe.secret.key"), "")
                .contentType("application/x-www-form-urlencoded")
                .formParams(
                        Map.of(
                                "amount", amount,
                                "currency", currency,
                                "payment_method_types[]", "card"
                        )
                )
                .post("/payment_intents");
    }
}