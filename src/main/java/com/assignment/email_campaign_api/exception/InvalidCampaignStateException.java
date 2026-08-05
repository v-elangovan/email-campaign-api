package com.assignment.email_campaign_api.exception;

public class InvalidCampaignStateException extends RuntimeException {

    public InvalidCampaignStateException(String message) {
        super(message);
    }
}
