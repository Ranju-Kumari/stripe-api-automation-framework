package com.stripe.api.models.responses.payments;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateInvoiceResponse {
    private String id;
    private String customer;
    private String status;
}
