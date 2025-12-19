package com.stripe.api.models.responses.customers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerResponse {
    private String id;
    private String name;
    private String email;
}
