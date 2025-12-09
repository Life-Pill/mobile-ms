package com.lifepill.prescription.service;

import com.lifepill.prescription.dto.request.PrescriptionUploadRequest;
import com.lifepill.prescription.dto.response.PrescriptionResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface PrescriptionService {
    
    PrescriptionResponse uploadPrescription(PrescriptionUploadRequest request, MultipartFile file);
    
    List<PrescriptionResponse> getUserPrescriptions(UUID userId);
    
    PrescriptionResponse getPrescription(UUID prescriptionId);
}
