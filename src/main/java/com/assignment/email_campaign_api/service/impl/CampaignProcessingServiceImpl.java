package com.assignment.email_campaign_api.service.impl;

import com.assignment.email_campaign_api.entity.Campaign;
import com.assignment.email_campaign_api.enums.CampaignStatus;
import com.assignment.email_campaign_api.repository.CampaignRepository;
import com.assignment.email_campaign_api.service.CampaignProcessingService;
import com.assignment.email_campaign_api.service.CampaignProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CampaignProcessingServiceImpl
        implements CampaignProcessingService {

    private final CampaignRepository campaignRepository;
    private final CampaignProcessor campaignProcessor;

    @Override
    public void processScheduledCampaigns() {

        log.info("Searching for campaigns ready for processing...");

        List<Campaign> campaigns =
                campaignRepository.findByStatusAndScheduledAtLessThanEqual(
                        CampaignStatus.SCHEDULED,
                        LocalDateTime.now());

        if (campaigns.isEmpty()) {

            log.info("No campaigns available for processing.");

            return;
        }

        log.info("Found {} campaign(s) ready for processing.",
                campaigns.size());

        campaigns.forEach(campaign -> {

            try {

                campaignProcessor.processCampaign(campaign.getId());

            } catch (Exception ex) {

                log.error("Failed to process campaign {}",
                        campaign.getId(),
                        ex);
            }

        });

        log.info("Campaign batch processing finished.");
    }
}