package com.assignment.email_campaign_api.controller;

import com.assignment.email_campaign_api.dto.request.CreateCampaignRequest;
import com.assignment.email_campaign_api.dto.response.CampaignResponse;
import com.assignment.email_campaign_api.enums.CampaignStatus;
import com.assignment.email_campaign_api.service.CampaignProcessingService;
import com.assignment.email_campaign_api.service.CampaignService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CampaignController.class)
class CampaignControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CampaignService campaignService;

    @MockitoBean
    private CampaignProcessingService campaignProcessingService;

    @Test
    void shouldCreateCampaign() throws Exception {

        CreateCampaignRequest request = new CreateCampaignRequest();
        request.setName("Summer Sale");
        request.setSubject("50% OFF");
        request.setSenderEmail("admin@test.com");
        request.setContent("Welcome");
        request.setScheduledAt(LocalDateTime.now().plusDays(1));

        CampaignResponse response = CampaignResponse.builder()
                .id(1L)
                .name("Summer Sale")
                .subject("50% OFF")
                .senderEmail("admin@test.com")
                .content("Welcome")
                .scheduledAt(request.getScheduledAt())
                .status(CampaignStatus.DRAFT)
                .createdAt(LocalDateTime.now())
                .build();

        when(campaignService.createCampaign(any()))
                .thenReturn(response);

        mockMvc.perform(post("/api/campaigns")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Summer Sale"));
    }

    @Test
    void shouldReturn400ForInvalidCampaign() throws Exception {

        CreateCampaignRequest request = new CreateCampaignRequest();

        mockMvc.perform(post("/api/campaigns")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
