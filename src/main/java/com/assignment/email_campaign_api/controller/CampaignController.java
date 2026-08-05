package com.assignment.email_campaign_api.controller;

import com.assignment.email_campaign_api.dto.request.CreateCampaignRequest;
import com.assignment.email_campaign_api.dto.response.ApiResponse;
import com.assignment.email_campaign_api.dto.response.CampaignDetailsResponse;
import com.assignment.email_campaign_api.dto.response.CampaignResponse;
import com.assignment.email_campaign_api.dto.response.CampaignStatisticsResponse;
import com.assignment.email_campaign_api.enums.CampaignStatus;
import com.assignment.email_campaign_api.service.CampaignProcessingService;
import com.assignment.email_campaign_api.service.CampaignService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Campaign APIs",
        description = "Operations related to email campaigns"
)
@RestController
@RequestMapping("/api/campaigns")
@RequiredArgsConstructor
public class CampaignController {

    private final CampaignService campaignService;
    private final CampaignProcessingService campaignProcessingService;
    @Operation(summary = "Create a new campaign")
    @PostMapping
    public ResponseEntity<ApiResponse<CampaignResponse>> createCampaign(
            @Valid @RequestBody CreateCampaignRequest request) {

        CampaignResponse response = campaignService.createCampaign(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        true,
                        "Campaign created successfully",
                        response));
    }
    @Operation(summary = "Schedule a campaign")
    @PostMapping("/{campaignId}/schedule")
    public ResponseEntity<ApiResponse<Void>> scheduleCampaign(
            @PathVariable Long campaignId) {

        campaignService.scheduleCampaign(campaignId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Campaign scheduled successfully",
                        null));
    }

    @Operation(summary = "Process scheduled campaigns")
    @PostMapping("/process")
    public ResponseEntity<ApiResponse<Void>> processCampaigns() {

        campaignProcessingService.processScheduledCampaigns();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Campaign processing completed",
                        null));
    }

    @Operation(summary = "List campaigns")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<CampaignResponse>>> getCampaigns(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "createdAt") String sortBy,

            @RequestParam(defaultValue = "desc") String direction,

            @RequestParam(required = false) CampaignStatus status,

            @RequestParam(required = false) String search) {

        Page<CampaignResponse> campaigns = campaignService.getCampaigns(
                page,
                size,
                sortBy,
                direction,
                status,
                search);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Campaigns fetched successfully",
                        campaigns
                ));
    }

    @Operation(summary = "Get campaign details")
    @GetMapping("/{campaignId}")
    public ResponseEntity<ApiResponse<CampaignDetailsResponse>> getCampaign(
            @PathVariable Long campaignId) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Campaign details",
                        campaignService.getCampaign(campaignId)));
    }

    @Operation(summary = "Get campaign statistics")
    @GetMapping("/{campaignId}/statistics")
    public ResponseEntity<ApiResponse<CampaignStatisticsResponse>> statistics(
            @PathVariable Long campaignId) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Campaign statistics",
                        campaignService.getStatistics(campaignId)));
    }

}