package com.assignment.email_campaign_api.service.impl;

import com.assignment.email_campaign_api.dto.request.AddRecipientsRequest;
import com.assignment.email_campaign_api.dto.request.RecipientRequest;
import com.assignment.email_campaign_api.entity.Campaign;
import com.assignment.email_campaign_api.enums.CampaignStatus;
import com.assignment.email_campaign_api.exception.DuplicateRecipientException;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipientServiceImplTest {

    @Mock
    private CampaignRepository campaignRepository;

    @Mock
    private RecipientRepository recipientRepository;

    @InjectMocks
    private RecipientServiceImpl recipientService;

    private AddRecipientsRequest buildRequest() {

        RecipientRequest recipient = new RecipientRequest();
        recipient.setName("John");
        recipient.setEmail("john@test.com");

        AddRecipientsRequest request = new AddRecipientsRequest();
        request.setRecipients(List.of(recipient));

        return request;
    }

    private Campaign buildCampaign() {

        return Campaign.builder()
                .id(1L)
                .name("Campaign")
                .subject("Subject")
                .senderEmail("admin@test.com")
                .content("Content")
                .scheduledAt(LocalDateTime.now().plusDays(1))
                .status(CampaignStatus.DRAFT)
                .build();
    }

    @Test
    void shouldAddRecipientsSuccessfully() {

        when(campaignRepository.findById(1L))
                .thenReturn(Optional.of(buildCampaign()));

        when(recipientRepository.existsByCampaignIdAndEmail(
                anyLong(),
                anyString()))
                .thenReturn(false);

        recipientService.addRecipients(1L, buildRequest());

        verify(recipientRepository)
                .saveAll(anyList());
    }

    @Test
    void shouldThrowExceptionWhenCampaignNotFound() {

        when(campaignRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> recipientService.addRecipients(1L, buildRequest())
        );
    }

    @Test
    void shouldThrowExceptionWhenDuplicateExistsInDatabase() {

        when(campaignRepository.findById(1L))
                .thenReturn(Optional.of(buildCampaign()));

        when(recipientRepository.existsByCampaignIdAndEmail(
                anyLong(),
                anyString()))
                .thenReturn(true);

        assertThrows(
                DuplicateRecipientException.class,
                () -> recipientService.addRecipients(1L, buildRequest())
        );
    }

    @Test
    void shouldThrowExceptionWhenDuplicateExistsInSameRequest() {

        RecipientRequest r1 = new RecipientRequest();
        r1.setName("John");
        r1.setEmail("john@test.com");

        RecipientRequest r2 = new RecipientRequest();
        r2.setName("John");
        r2.setEmail("john@test.com");

        AddRecipientsRequest request = new AddRecipientsRequest();
        request.setRecipients(List.of(r1, r2));

        when(campaignRepository.findById(1L))
                .thenReturn(Optional.of(buildCampaign()));

        assertThrows(
                DuplicateRecipientException.class,
                () -> recipientService.addRecipients(1L, request)
        );
    }
}