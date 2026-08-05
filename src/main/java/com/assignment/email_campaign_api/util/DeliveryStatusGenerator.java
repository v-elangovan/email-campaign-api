package com.assignment.email_campaign_api.util;

import com.assignment.email_campaign_api.enums.RecipientStatus;

import java.security.SecureRandom;

public final class DeliveryStatusGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();

    private DeliveryStatusGenerator() {
    }

    public static RecipientStatus generateStatus() {

        return RANDOM.nextBoolean()
                ? RecipientStatus.DELIVERED
                : RecipientStatus.FAILED;
    }

}