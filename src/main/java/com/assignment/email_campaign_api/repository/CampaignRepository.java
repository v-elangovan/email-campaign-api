package com.assignment.email_campaign_api.repository;

import com.assignment.email_campaign_api.entity.Campaign;
import com.assignment.email_campaign_api.enums.CampaignStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {

    // Pagination + Filter
    Page<Campaign> findByStatus(CampaignStatus status, Pageable pageable);

    // Pagination + Search
    Page<Campaign> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // Pagination + Search + Status
    Page<Campaign> findByStatusAndNameContainingIgnoreCase(
            CampaignStatus status,
            String name,
            Pageable pageable
    );

    // Lock campaign while processing
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT c
            FROM Campaign c
            WHERE c.id = :id
            """)
    Optional<Campaign> findByIdForUpdate(@Param("id") Long id);

    // Find campaigns that are ready to process
    List<Campaign> findByStatusAndScheduledAtLessThanEqual(
            CampaignStatus status,
            LocalDateTime scheduledAt
    );
}