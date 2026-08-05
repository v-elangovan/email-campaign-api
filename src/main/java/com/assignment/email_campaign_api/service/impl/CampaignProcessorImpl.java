package com.assignment.email_campaign_api.service.impl;

import com.assignment.email_campaign_api.entity.Campaign;
import com.assignment.email_campaign_api.entity.Recipient;
import com.assignment.email_campaign_api.enums.CampaignStatus;
import com.assignment.email_campaign_api.enums.RecipientStatus;
import com.assignment.email_campaign_api.exception.ResourceNotFoundException;
import com.assignment.email_campaign_api.repository.CampaignRepository;
import com.assignment.email_campaign_api.repository.RecipientRepository;
import com.assignment.email_campaign_api.service.CampaignProcessor;
import com.assignment.email_campaign_api.util.DeliveryStatusGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CampaignProcessorImpl implements CampaignProcessor {

    private final CampaignRepository campaignRepository;
    private final RecipientRepository recipientRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processCampaign(Long campaignId) {

        Campaign campaign = campaignRepository.findByIdForUpdate(campaignId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Campaign not found"));

        if (campaign.getStatus() != CampaignStatus.SCHEDULED) {

            log.warn("Campaign {} already processed.", campaignId);

            return;
        }

        List<Recipient> recipients =
                recipientRepository.findByCampaignId(campaignId);

        log.info("Processing campaign {} with {} recipients",
                campaignId,
                recipients.size());

        for (Recipient recipient : recipients) {
            recipient.setStatus(
                    DeliveryStatusGenerator.generateStatus());
        }

        recipientRepository.saveAll(recipients);

        campaign.setStatus(CampaignStatus.COMPLETED);

        log.info("Campaign {} completed successfully.", campaignId);
    }
}