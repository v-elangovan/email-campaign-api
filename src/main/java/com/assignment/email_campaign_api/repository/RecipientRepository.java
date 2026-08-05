package com.assignment.email_campaign_api.repository;

import com.assignment.email_campaign_api.entity.Recipient;
import com.assignment.email_campaign_api.enums.RecipientStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipientRepository extends JpaRepository<Recipient, Long> {

    boolean existsByCampaignIdAndEmail(Long campaignId, String email);

    long countByCampaignId(Long campaignId);

    long countByCampaignIdAndStatus(Long campaignId, RecipientStatus status);

    List<Recipient> findByCampaignId(Long campaignId);

}