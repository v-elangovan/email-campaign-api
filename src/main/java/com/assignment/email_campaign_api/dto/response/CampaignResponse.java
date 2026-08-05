package com.assignment.email_campaign_api.dto.response;

import com.assignment.email_campaign_api.enums.CampaignStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CampaignResponse {

    private Long id;

    private String name;

    private String subject;

    private String senderEmail;

    private String content;

    private LocalDateTime scheduledAt;

    private CampaignStatus status;

    private LocalDateTime createdAt;
}