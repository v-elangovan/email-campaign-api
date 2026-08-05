package com.assignment.email_campaign_api.controller;

import com.assignment.email_campaign_api.dto.request.AddRecipientsRequest;
import com.assignment.email_campaign_api.dto.response.ApiResponse;
import com.assignment.email_campaign_api.service.RecipientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Recipient APIs",
        description = "Operations related to campaign recipients"
)
@RestController
@RequestMapping("/api/campaigns")
@RequiredArgsConstructor
public class RecipientController {

    private final RecipientService recipientService;

    @Operation(summary = "Add recipients to a campaign")
    @PostMapping("/{campaignId}/recipients")
    public ResponseEntity<ApiResponse<Void>> addRecipients(

            @PathVariable Long campaignId,

            @Valid
            @RequestBody
            AddRecipientsRequest request) {

        recipientService.addRecipients(campaignId, request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Recipients added successfully",
                        null));
    }

}