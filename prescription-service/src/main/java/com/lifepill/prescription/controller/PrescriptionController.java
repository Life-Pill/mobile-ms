package com.lifepill.prescription.controller;

import com.lifepill.prescription.dto.request.PrescriptionUploadRequest;
import com.lifepill.prescription.dto.response.PrescriptionResponse;
import com.lifepill.prescription.service.PrescriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/prescriptions")
@RequiredArgsConstructor
@Tag(name = "Prescription API", description = "Endpoints for managing prescriptions")
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

    @Operation(summary = "Get all prescriptions for a user")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PrescriptionResponse>> getUserPrescriptions(@PathVariable UUID userId) {
        return ResponseEntity.ok(prescriptionService.getUserPrescriptions(userId));
    }

    @Operation(summary = "Get prescription details by ID")
    @GetMapping("/{prescriptionId}")
    public ResponseEntity<PrescriptionResponse> getPrescription(@PathVariable UUID prescriptionId) {
        return ResponseEntity.ok(prescriptionService.getPrescription(prescriptionId));
    }
}
