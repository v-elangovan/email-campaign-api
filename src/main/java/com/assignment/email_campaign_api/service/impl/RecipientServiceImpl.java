package com.assignment.email_campaign_api.service.impl;

import com.assignment.email_campaign_api.dto.request.AddRecipientsRequest;
import com.assignment.email_campaign_api.dto.request.RecipientRequest;
import com.assignment.email_campaign_api.entity.Campaign;
import com.assignment.email_campaign_api.entity.Recipient;
import com.assignment.email_campaign_api.enums.RecipientStatus;
import com.assignment.email_campaign_api.exception.DuplicateRecipientException;
import com.assignment.email_campaign_api.exception.ResourceNotFoundException;
import com.assignment.email_campaign_api.repository.CampaignRepository;
import com.assignment.email_campaign_api.repository.RecipientRepository;
import com.assignment.email_campaign_api.service.RecipientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RecipientServiceImpl implements RecipientService {

    private final CampaignRepository campaignRepository;
    private final RecipientRepository recipientRepository;

    @Override
    public void addRecipients(Long campaignId, AddRecipientsRequest request) {

        log.info("Adding {} recipients to campaign {}",
                request.getRecipients().size(),
                campaignId);

        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Campaign not found"));

        List<Recipient> recipients = new ArrayList<>();

        Set<String> requestEmails = new HashSet<>();

        for (RecipientRequest recipientRequest : request.getRecipients()) {

            String email = recipientRequest.getEmail()
                    .trim()
                    .toLowerCase();

            // Duplicate inside current request
            if (!requestEmails.add(email)) {

                throw new DuplicateRecipientException(
                        "Duplicate email in request: " + email
                );
            }

            // Duplicate already stored in database
            if (recipientRepository.existsByCampaignIdAndEmail(
                    campaignId,
                    email)) {

                throw new DuplicateRecipientException(
                        "Recipient with email "
                                + email
                                + " already exists");
            }

            Recipient recipient = Recipient.builder()
                    .campaign(campaign)
                    .name(recipientRequest.getName())
                    .email(email)
                    .status(RecipientStatus.PENDING)
                    .build();

            recipients.add(recipient);
        }

        recipientRepository.saveAll(recipients);

        log.info("{} recipients added successfully to campaign {}",
                recipients.size(),
                campaignId);
    }
}