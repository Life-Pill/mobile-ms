package com.lifepill.prescription.service;

import com.lifepill.prescription.dto.request.BranchResponseRequest;
import com.lifepill.prescription.dto.request.MobilePrescriptionUploadRequest;
import com.lifepill.prescription.dto.request.PlaceOrderRequest;
import com.lifepill.prescription.dto.request.PrescriptionUploadRequest;
import com.lifepill.prescription.dto.response.BranchResponseDTO;
import com.lifepill.prescription.dto.response.OrderPlacementResponse;
import com.lifepill.prescription.dto.response.PrescriptionResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface PrescriptionService {
    
    // Prescription CRUD operations
    PrescriptionResponse uploadPrescription(PrescriptionUploadRequest request, MultipartFile file);
    
    PrescriptionResponse uploadPrescriptionMobile(MobilePrescriptionUploadRequest request);
    
    PrescriptionResponse getPrescription(UUID prescriptionId);
    
    PrescriptionResponse getPrescriptionWithResponses(UUID prescriptionId);
    
    List<PrescriptionResponse> getUserPrescriptions(UUID userId);
    
    PrescriptionResponse updatePrescription(UUID prescriptionId, PrescriptionUploadRequest request, UUID authenticatedUserId);
    
    void deletePrescription(UUID prescriptionId, UUID authenticatedUserId);
    
    // Branch response operations
    BranchResponseDTO submitBranchResponse(UUID prescriptionId, BranchResponseRequest request);
    
    BranchResponseDTO updateBranchResponse(UUID prescriptionId, UUID responseId, BranchResponseRequest request);
    
    List<BranchResponseDTO> getPrescriptionResponses(UUID prescriptionId);
    
    BranchResponseDTO getResponseById(UUID prescriptionId, UUID responseId);
    
    // Order placement
    OrderPlacementResponse placeOrder(UUID prescriptionId, PlaceOrderRequest request);
}
