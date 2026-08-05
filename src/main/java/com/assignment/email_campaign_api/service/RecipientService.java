package com.assignment.email_campaign_api.service;

import com.assignment.email_campaign_api.dto.request.AddRecipientsRequest;

public interface RecipientService {

    void addRecipients(Long campaignId,
                       AddRecipientsRequest request);

}