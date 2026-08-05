package com.assignment.email_campaign_api.util;

import com.assignment.email_campaign_api.dto.response.CampaignResponse;
import com.assignment.email_campaign_api.entity.Campaign;

public final class CampaignMapper {

    private CampaignMapper() {
    }

    public static CampaignResponse toResponse(Campaign campaign) {

        return CampaignResponse.builder()
                .id(campaign.getId())
                .name(campaign.getName())
                .subject(campaign.getSubject())
                .senderEmail(campaign.getSenderEmail())
                .content(campaign.getContent())
                .scheduledAt(campaign.getScheduledAt())
                .status(campaign.getStatus())
                .createdAt(campaign.getCreatedAt())
                .build();
    }

}