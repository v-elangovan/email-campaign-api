package com.assignment.email_campaign_api.exception;

public class DuplicateRecipientException extends RuntimeException {

    public DuplicateRecipientException(String message) {
        super(message);
    }
}