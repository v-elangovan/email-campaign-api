package com.assignment.email_campaign_api.service.impl;

import com.assignment.email_campaign_api.dto.request.CreateCampaignRequest;
import com.assignment.email_campaign_api.entity.Campaign;
import com.assignment.email_campaign_api.enums.CampaignStatus;
import com.assignment.email_campaign_api.exception.InvalidCampaignStateException;
import com.assignment.email_campaign_api.repository.CampaignRepository;
import com.assignment.email_campaign_api.repository.RecipientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
@ExtendWith(MockitoExtension.class)
class CampaignServiceImplTest {

    @Mock
    private CampaignRepository campaignRepository;

    @Mock
    private RecipientRepository recipientRepository;

    @InjectMocks
    private CampaignServiceImpl campaignService;

    private CreateCampaignRequest request;

    @BeforeEach
    void setup() {

        request = new CreateCampaignRequest();
        request.setName("Diwali Offer");
        request.setSubject("50% Discount");
        request.setSenderEmail("marketing@test.com");
        request.setContent("Welcome");
        request.setScheduledAt(LocalDateTime.now().plusDays(1));
    }
    @Test
    void shouldCreateCampaign() {

        Campaign savedCampaign = Campaign.builder()
                .id(1L)
                .name(request.getName())
                .subject(request.getSubject())
                .senderEmail(request.getSenderEmail())
                .content(request.getContent())
                .scheduledAt(request.getScheduledAt())
                .status(CampaignStatus.DRAFT)
                .createdAt(LocalDateTime.now())
                .build();

        when(campaignRepository.save(any(Campaign.class)))
                .thenReturn(savedCampaign);

        var response = campaignService.createCampaign(request);

        assertNotNull(response);
        assertEquals("Diwali Offer", response.getName());
        assertEquals(CampaignStatus.DRAFT, response.getStatus());

        verify(campaignRepository).save(any(Campaign.class));
    }
    @Test
    void shouldScheduleCampaign() {

        Campaign campaign = Campaign.builder()
                .id(1L)
                .status(CampaignStatus.DRAFT)
                .scheduledAt(LocalDateTime.now().plusHours(2))
                .build();

        when(campaignRepository.findById(1L))
                .thenReturn(Optional.of(campaign));

        when(recipientRepository.countByCampaignId(1L))
                .thenReturn(5L);

        campaignService.scheduleCampaign(1L);

        assertEquals(CampaignStatus.SCHEDULED, campaign.getStatus());

        verify(campaignRepository).findById(1L);
    }
    @Test
    void shouldThrowExceptionWhenNoRecipients() {

        Campaign campaign = Campaign.builder()
                .id(1L)
                .status(CampaignStatus.DRAFT)
                .scheduledAt(LocalDateTime.now().plusHours(2))
                .build();

        when(campaignRepository.findById(1L))
                .thenReturn(Optional.of(campaign));

        when(recipientRepository.countByCampaignId(1L))
                .thenReturn(0L);

        assertThrows(
                InvalidCampaignStateException.class,
                () -> campaignService.scheduleCampaign(1L)
        );
    }
    @Test
    void shouldThrowExceptionWhenCampaignAlreadyScheduled() {

        Campaign campaign = Campaign.builder()
                .id(1L)
                .status(CampaignStatus.SCHEDULED)
                .scheduledAt(LocalDateTime.now().plusHours(2))
                .build();

        when(campaignRepository.findById(1L))
                .thenReturn(Optional.of(campaign));

        assertThrows(
                InvalidCampaignStateException.class,
                () -> campaignService.scheduleCampaign(1L)
        );
    }
    @Test
    void shouldThrowExceptionWhenScheduledTimeIsPast() {

        Campaign campaign = Campaign.builder()
                .id(1L)
                .status(CampaignStatus.DRAFT)
                .scheduledAt(LocalDateTime.now().minusHours(1))
                .build();

        when(campaignRepository.findById(1L))
                .thenReturn(Optional.of(campaign));

        assertThrows(
                InvalidCampaignStateException.class,
                () -> campaignService.scheduleCampaign(1L)
        );
    }
}