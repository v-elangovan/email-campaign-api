package com.assignment.email_campaign_api.controller;

import com.assignment.email_campaign_api.dto.request.AddRecipientsRequest;
import com.assignment.email_campaign_api.dto.request.RecipientRequest;
import com.assignment.email_campaign_api.service.RecipientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecipientController.class)
class RecipientControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    RecipientService recipientService;

    @Test
    void shouldAddRecipients() throws Exception {

        RecipientRequest recipient = new RecipientRequest();
        recipient.setName("John");
        recipient.setEmail("john@test.com");

        AddRecipientsRequest request = new AddRecipientsRequest();
        request.setRecipients(List.of(recipient));

        mockMvc.perform(post("/api/campaigns/1/recipients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}