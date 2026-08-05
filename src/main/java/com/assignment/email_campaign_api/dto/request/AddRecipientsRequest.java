package com.assignment.email_campaign_api.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class AddRecipientsRequest {

    @Valid
    @NotEmpty(message = "Recipient list cannot be empty")
    private List<RecipientRequest> recipients;
}