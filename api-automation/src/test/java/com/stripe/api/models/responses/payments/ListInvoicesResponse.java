package com.stripe.api.models.responses.payments;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

/**
 * Represents the response from Stripe API when listing invoices.
 * This response contains a list of invoices with pagination support.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListInvoicesResponse {

    @JsonProperty("object")
    private String object;

    @JsonProperty("url")
    private String url;

    @JsonProperty("has_more")
    private Boolean hasMore;

    @JsonProperty("data")
    private List<InvoiceData> data;
}

