package com.assignment.email_campaign_api.service.impl;

import com.assignment.email_campaign_api.dto.request.CreateCampaignRequest;
import com.assignment.email_campaign_api.dto.response.CampaignDetailsResponse;
import com.assignment.email_campaign_api.dto.response.CampaignResponse;
import com.assignment.email_campaign_api.dto.response.CampaignStatisticsResponse;
import com.assignment.email_campaign_api.entity.Campaign;
import com.assignment.email_campaign_api.enums.CampaignStatus;
import com.assignment.email_campaign_api.enums.RecipientStatus;
import com.assignment.email_campaign_api.exception.BadRequestException;
import com.assignment.email_campaign_api.exception.InvalidCampaignStateException;
import com.assignment.email_campaign_api.exception.ResourceNotFoundException;
import com.assignment.email_campaign_api.repository.CampaignRepository;
import com.assignment.email_campaign_api.repository.RecipientRepository;
import com.assignment.email_campaign_api.service.CampaignService;
import com.assignment.email_campaign_api.util.CampaignMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CampaignServiceImpl implements CampaignService {

    private final CampaignRepository campaignRepository;
    private final RecipientRepository recipientRepository;

    @Override
    public CampaignResponse createCampaign(CreateCampaignRequest request) {

        log.info("Creating campaign : {}", request.getName());

        Campaign campaign = Campaign.builder()
                .name(request.getName())
                .subject(request.getSubject())
                .senderEmail(request.getSenderEmail())
                .content(request.getContent())
                .scheduledAt(request.getScheduledAt())
                .status(CampaignStatus.DRAFT)
                .build();

        campaign = campaignRepository.save(campaign);

        log.info("Campaign {} created successfully", campaign.getId());

        return CampaignMapper.toResponse(campaign);
    }

    @Override
    public void scheduleCampaign(Long campaignId) {

        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Campaign not found"));

        if (campaign.getStatus() != CampaignStatus.DRAFT) {
            throw new InvalidCampaignStateException(
                    "Only draft campaigns can be scheduled");
        }

        if (campaign.getScheduledAt() == null ||
                !campaign.getScheduledAt().isAfter(LocalDateTime.now())) {

            throw new InvalidCampaignStateException(
                    "Scheduled time must be in the future");
        }

        long recipients = recipientRepository.countByCampaignId(campaignId);

        if (recipients == 0) {
            throw new InvalidCampaignStateException(
                    "Campaign must have at least one recipient");
        }

        campaign.setStatus(CampaignStatus.SCHEDULED);

        log.info("Campaign {} scheduled successfully", campaignId);
    }

    @Override
    @Transactional(readOnly = true)
    public CampaignDetailsResponse getCampaign(Long campaignId) {

        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Campaign not found"));

        return CampaignDetailsResponse.builder()
                .id(campaign.getId())
                .name(campaign.getName())
                .subject(campaign.getSubject())
                .senderEmail(campaign.getSenderEmail())
                .content(campaign.getContent())
                .scheduledAt(campaign.getScheduledAt())
                .status(campaign.getStatus())
                .createdAt(campaign.getCreatedAt())
                .totalRecipients(
                        recipientRepository.countByCampaignId(campaignId))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public CampaignStatisticsResponse getStatistics(Long campaignId) {

        campaignRepository.findById(campaignId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Campaign not found"));

        long total = recipientRepository.countByCampaignId(campaignId);

        long delivered = recipientRepository.countByCampaignIdAndStatus(
                campaignId,
                RecipientStatus.DELIVERED);

        long failed = recipientRepository.countByCampaignIdAndStatus(
                campaignId,
                RecipientStatus.FAILED);

        long pending = recipientRepository.countByCampaignIdAndStatus(
                campaignId,
                RecipientStatus.PENDING);

        return CampaignStatisticsResponse.builder()
                .campaignId(campaignId)
                .totalRecipients(total)
                .delivered(delivered)
                .failed(failed)
                .pending(pending)
                .build();
    }
    @Override
    @Transactional(readOnly = true)
    public Page<CampaignResponse> getCampaigns(
            int page,
            int size,
            String sortBy,
            String direction,
            CampaignStatus status,
            String search) {

        Set<String> allowedSortFields = Set.of(
                "createdAt",
                "name",
                "status",
                "scheduledAt"
        );

        if (!allowedSortFields.contains(sortBy)) {
            throw new BadRequestException(
                    "Invalid sort field: " + sortBy
            );
        }

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Campaign> campaigns;

        if (status != null && search != null && !search.isBlank()) {

            campaigns = campaignRepository
                    .findByStatusAndNameContainingIgnoreCase(
                            status,
                            search,
                            pageable);

        } else if (status != null) {

            campaigns = campaignRepository
                    .findByStatus(status, pageable);

        } else if (search != null && !search.isBlank()) {

            campaigns = campaignRepository
                    .findByNameContainingIgnoreCase(search, pageable);

        } else {

            campaigns = campaignRepository.findAll(pageable);
        }

        return campaigns.map(CampaignMapper::toResponse);
    }

}