package com.stripe.api.models.requests.customers;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerRequest {
    private String name;
    private String email;
}
