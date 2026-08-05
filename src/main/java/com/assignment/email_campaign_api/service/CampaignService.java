package com.assignment.email_campaign_api.service;

import com.assignment.email_campaign_api.dto.request.CreateCampaignRequest;
import com.assignment.email_campaign_api.dto.response.CampaignDetailsResponse;
import com.assignment.email_campaign_api.dto.response.CampaignResponse;
import com.assignment.email_campaign_api.dto.response.CampaignStatisticsResponse;
import com.assignment.email_campaign_api.enums.CampaignStatus;
import org.springframework.data.domain.Page;

public interface CampaignService {

    CampaignResponse createCampaign(CreateCampaignRequest request);

    void scheduleCampaign(Long campaignId);

    Page<CampaignResponse> getCampaigns(
            int page,
            int size,
            String sortBy,
            String direction,
            CampaignStatus status,
            String search);

    CampaignDetailsResponse getCampaign(Long campaignId);

    CampaignStatisticsResponse getStatistics(Long campaignId);
}