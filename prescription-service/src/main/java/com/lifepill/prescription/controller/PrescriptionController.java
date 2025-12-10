package com.lifepill.prescription.controller;

import com.lifepill.prescription.dto.request.BranchResponseRequest;
import com.lifepill.prescription.dto.request.MobilePrescriptionUploadRequest;
import com.lifepill.prescription.dto.request.PrescriptionUploadRequest;
import com.lifepill.prescription.dto.response.ApiResponse;
import com.lifepill.prescription.dto.response.BranchResponseDTO;
import com.lifepill.prescription.dto.response.PrescriptionResponse;
import com.lifepill.prescription.service.PrescriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/lifepill/v1/prescription")
@RequiredArgsConstructor
@Tag(name = "Prescription API", description = "Endpoints for managing prescriptions and branch responses")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @Operation(summary = "Upload a new prescription (image + metadata)")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PrescriptionResponse> uploadPrescription(
            @RequestPart("file") MultipartFile file,
            @RequestParam("userId") UUID userId,
            @RequestParam(value = "notes", required = false) String notes) {
        
        PrescriptionUploadRequest request = PrescriptionUploadRequest.builder()
                .userId(userId)
                .notes(notes)
                .build();
        
        return new ResponseEntity<>(prescriptionService.uploadPrescription(request, file), HttpStatus.CREATED);
    }

    @Operation(summary = "Upload prescription from mobile (saves to DB only)",
            description = "Mobile endpoint that accepts prescription image with userId and notes, and saves directly to database")
    @PostMapping(value = "/upload-mobile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<PrescriptionResponse>> uploadPrescriptionMobile(
            @RequestPart("file") MultipartFile file,
            @RequestParam("userId") UUID userId,
            @RequestParam(value = "notes", required = false) String notes) {
        
        PrescriptionResponse response = prescriptionService.uploadPrescriptionMobile(userId, notes, file);
        
        return new ResponseEntity<>(
                ApiResponse.success("Prescription uploaded successfully", response),
                HttpStatus.CREATED
        );
    }

    @Operation(summary = "Get all prescriptions for a user")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PrescriptionResponse>> getUserPrescriptions(@PathVariable UUID userId) {
        return ResponseEntity.ok(prescriptionService.getUserPrescriptions(userId));
    }

    @Operation(summary = "Get prescription details by ID (without full responses)")
    @GetMapping("/{prescriptionId}")
    public ResponseEntity<PrescriptionResponse> getPrescription(@PathVariable UUID prescriptionId) {
        return ResponseEntity.ok(prescriptionService.getPrescription(prescriptionId));
    }
    
    @Operation(summary = "Get prescription details by ID with all branch responses")
    @GetMapping("/{prescriptionId}/full")
    public ResponseEntity<PrescriptionResponse> getPrescriptionWithResponses(@PathVariable UUID prescriptionId) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionWithResponses(prescriptionId));
    }

    @Operation(summary = "Update prescription notes")
    @PutMapping("/{prescriptionId}")
    public ResponseEntity<PrescriptionResponse> updatePrescription(
            @PathVariable UUID prescriptionId,
            @RequestParam String notes,
            @RequestParam UUID userId,
            HttpServletRequest request) {
        
        UUID authenticatedUserId = (UUID) request.getAttribute("userId");
        
        PrescriptionUploadRequest updateRequest = PrescriptionUploadRequest.builder()
                .userId(userId)
                .notes(notes)
                .build();
        
        return ResponseEntity.ok(prescriptionService.updatePrescription(prescriptionId, updateRequest, authenticatedUserId));
    }

    @Operation(summary = "Delete prescription")
    @DeleteMapping("/{prescriptionId}")
    public ResponseEntity<Void> deletePrescription(
            @PathVariable UUID prescriptionId,
            HttpServletRequest request) {
        
        UUID authenticatedUserId = (UUID) request.getAttribute("userId");
        prescriptionService.deletePrescription(prescriptionId, authenticatedUserId);
        
        return ResponseEntity.noContent().build();
    }
    
    // ======================== Branch Response Endpoints ========================
    
    @Operation(summary = "Submit branch response to a prescription",
            description = "POS/Branch pharmacist submits medicine availability and pricing for a prescription")
    @PostMapping("/{prescriptionId}/responses")
    public ResponseEntity<BranchResponseDTO> submitBranchResponse(
            @PathVariable @Parameter(description = "Prescription ID") UUID prescriptionId,
            @Valid @RequestBody BranchResponseRequest request) {
        
        return new ResponseEntity<>(
                prescriptionService.submitBranchResponse(prescriptionId, request),
                HttpStatus.CREATED
        );
    }
    
    @Operation(summary = "Update existing branch response",
            description = "Branch can update their response with revised availability or pricing")
    @PutMapping("/{prescriptionId}/responses/{responseId}")
    public ResponseEntity<BranchResponseDTO> updateBranchResponse(
            @PathVariable @Parameter(description = "Prescription ID") UUID prescriptionId,
            @PathVariable @Parameter(description = "Response ID") UUID responseId,
            @Valid @RequestBody BranchResponseRequest request) {
        
        return ResponseEntity.ok(
                prescriptionService.updateBranchResponse(prescriptionId, responseId, request)
        );
    }
    
    @Operation(summary = "Get all branch responses for a prescription",
            description = "Retrieve all responses from different branches for a prescription")
    @GetMapping("/{prescriptionId}/responses")
    public ResponseEntity<List<BranchResponseDTO>> getPrescriptionResponses(
            @PathVariable @Parameter(description = "Prescription ID") UUID prescriptionId) {
        
        return ResponseEntity.ok(prescriptionService.getPrescriptionResponses(prescriptionId));
    }
    
    @Operation(summary = "Get specific branch response by ID")
    @GetMapping("/{prescriptionId}/responses/{responseId}")
    public ResponseEntity<BranchResponseDTO> getResponseById(
            @PathVariable @Parameter(description = "Prescription ID") UUID prescriptionId,
            @PathVariable @Parameter(description = "Response ID") UUID responseId) {
        
        return ResponseEntity.ok(prescriptionService.getResponseById(prescriptionId, responseId));
    }
    
    // ======================== Order Placement Endpoints ========================
    
    @Operation(summary = "Place order from prescription",
            description = "User selects a branch response and places an order. " +
                    "This creates an order and notifies the branch for fulfillment.")
    @PostMapping("/{prescriptionId}/order")
    public ResponseEntity<com.lifepill.prescription.dto.response.OrderPlacementResponse> placeOrder(
            @PathVariable @Parameter(description = "Prescription ID") UUID prescriptionId,
            @RequestBody @Parameter(description = "Order placement details") 
                com.lifepill.prescription.dto.request.PlaceOrderRequest request) {
        
        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED)
                .body(prescriptionService.placeOrder(prescriptionId, request));
    }
}

