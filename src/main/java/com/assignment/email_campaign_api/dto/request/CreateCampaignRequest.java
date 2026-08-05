package com.assignment.email_campaign_api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateCampaignRequest {

    @NotBlank(message = "Campaign name is required")
    private String name;

    @NotBlank(message = "Subject is required")
    private String subject;

    @Email(message = "Invalid sender email")
    @NotBlank(message = "Sender email is required")
    private String senderEmail;

    @NotBlank(message = "Content is required")
    private String content;

    @NotNull(message = "Scheduled time is required")
    @Future(message = "Scheduled time must be in the future")
    private LocalDateTime scheduledAt;
}
