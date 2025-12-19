package com.stripe.api.models.requests.customers;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateCustomerRequest {
    private String orderId;
}
