package com.stripe.api.models.responses.payments;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Represents individual invoice data from Stripe API list response
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceData {
    private String id;
    private String customer;
    private String status;
    private String number;
    private Long created;
    @JsonProperty("due_date")
    private Long dueDate;
    private Long total;
    private String currency;
    private String description;
}


