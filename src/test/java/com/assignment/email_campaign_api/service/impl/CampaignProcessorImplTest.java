package com.assignment.email_campaign_api.service.impl;


import com.assignment.email_campaign_api.entity.Campaign;
import com.assignment.email_campaign_api.entity.Recipient;
import com.assignment.email_campaign_api.enums.CampaignStatus;
import com.assignment.email_campaign_api.enums.RecipientStatus;
import com.assignment.email_campaign_api.exception.ResourceNotFoundException;
import com.assignment.email_campaign_api.repository.CampaignRepository;
import com.assignment.email_campaign_api.repository.RecipientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CampaignProcessorImplTest {

    @Mock
    private CampaignRepository campaignRepository;

    @Mock
    private RecipientRepository recipientRepository;

    @InjectMocks
    private CampaignProcessorImpl campaignProcessor;

    @Test
    void shouldProcessCampaignSuccessfully() {

        Campaign campaign = Campaign.builder()
                .id(1L)
                .status(CampaignStatus.SCHEDULED)
                .scheduledAt(LocalDateTime.now().minusMinutes(5))
                .build();

        Recipient recipient1 = Recipient.builder()
                .id(1L)
                .status(RecipientStatus.PENDING)
                .build();

        Recipient recipient2 = Recipient.builder()
                .id(2L)
                .status(RecipientStatus.PENDING)
                .build();

        when(campaignRepository.findByIdForUpdate(1L))
                .thenReturn(Optional.of(campaign));

        when(recipientRepository.findByCampaignId(1L))
                .thenReturn(List.of(recipient1, recipient2));

        campaignProcessor.processCampaign(1L);

        assertEquals(CampaignStatus.COMPLETED, campaign.getStatus());

        assertNotEquals(RecipientStatus.PENDING, recipient1.getStatus());
        assertNotEquals(RecipientStatus.PENDING, recipient2.getStatus());

        verify(recipientRepository).saveAll(anyList());
    }

    @Test
    void shouldIgnoreAlreadyProcessedCampaign() {

        Campaign campaign = Campaign.builder()
                .id(1L)
                .status(CampaignStatus.COMPLETED)
                .build();

        when(campaignRepository.findByIdForUpdate(1L))
                .thenReturn(Optional.of(campaign));

        campaignProcessor.processCampaign(1L);

        verify(recipientRepository, never())
                .findByCampaignId(anyLong());

        verify(recipientRepository, never())
                .saveAll(anyList());
    }

    @Test
    void shouldThrowExceptionWhenCampaignNotFound() {

        when(campaignRepository.findByIdForUpdate(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> campaignProcessor.processCampaign(1L)
        );
    }
}