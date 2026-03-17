package com.stripe.api.models.requests.payments;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateInvoiceRequest {
    private String customer;// customerId
}
