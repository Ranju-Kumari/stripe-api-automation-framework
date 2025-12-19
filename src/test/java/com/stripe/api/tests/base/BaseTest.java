package com.stripe.api.tests.base;

import com.stripe.utilities.ConfigManager;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

import static io.restassured.RestAssured.baseURI;

public class BaseTest {

    @BeforeClass
    public void setUp() {
        baseURI = ConfigManager.getKey("baseUrl");
        RestAssured.authentication = RestAssured.basic(ConfigManager.getKey("stripe.secret.key"), "");
        System.out.print("BASE URL :"+ baseURI);
    }
}
 