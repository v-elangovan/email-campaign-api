package com.assignment.email_campaign_api.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CampaignStatisticsResponse {

    private Long campaignId;

    private long totalRecipients;

    private long delivered;

    private long failed;

    private long pending;
}