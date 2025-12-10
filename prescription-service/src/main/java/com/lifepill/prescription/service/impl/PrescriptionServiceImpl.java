package com.lifepill.prescription.service.impl;

import com.lifepill.prescription.config.RabbitMQConfig;
import com.lifepill.prescription.dto.request.BranchResponseRequest;
import com.lifepill.prescription.dto.request.PrescriptionUploadRequest;
import com.lifepill.prescription.dto.response.BranchResponseDTO;
import com.lifepill.prescription.dto.response.MedicineAvailabilityDTO;
import com.lifepill.prescription.dto.response.PrescriptionResponse;
import com.lifepill.prescription.entity.MedicineAvailability;
import com.lifepill.prescription.entity.Prescription;
import com.lifepill.prescription.entity.PrescriptionResponse.ResponseStatus;
import com.lifepill.prescription.event.PrescriptionResponseEvent;
import com.lifepill.prescription.event.PrescriptionUploadedEvent;
import com.lifepill.prescription.repository.MedicineAvailabilityRepository;
import com.lifepill.prescription.repository.PrescriptionRepository;
import com.lifepill.prescription.repository.PrescriptionResponseRepository;
import com.lifepill.prescription.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final PrescriptionResponseRepository responseRepository;
    private final MedicineAvailabilityRepository medicineAvailabilityRepository;
    private final S3Client s3Client;
    private final RabbitTemplate rabbitTemplate;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Override
    @Transactional
    public PrescriptionResponse uploadPrescription(PrescriptionUploadRequest request, MultipartFile file) {
        // 1. Upload image to S3
        String key = "prescriptions/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
        try {
            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .build(),
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image to S3", e);
        }

        String imageUrl = "https://" + bucketName + ".s3.amazonaws.com/" + key;

        // 2. Save Metadata to DB
        Prescription prescription = Prescription.builder()
                .userId(request.getUserId())
                .imageUrl(imageUrl)
                .notes(request.getNotes())
                .status(Prescription.PrescriptionStatus.UPLOADED)
                .uploadTimestamp(LocalDateTime.now())
                .build();

        prescription = prescriptionRepository.save(prescription);

        // 3. Publish Event with proper DTO
        PrescriptionUploadedEvent event = PrescriptionUploadedEvent.builder()
                .prescriptionId(prescription.getId())
                .userId(prescription.getUserId())
                .imageUrl(prescription.getImageUrl())
                .notes(prescription.getNotes())
                .status(prescription.getStatus().name())
                .uploadTimestamp(prescription.getUploadTimestamp())
                .build();
        
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY_UPLOADED, event);
        log.info("Published prescription uploaded event for prescription: {}", prescription.getId());

        return mapToDTO(prescription);
    }

    @Override
    public List<PrescriptionResponse> getUserPrescriptions(UUID userId) {
        return prescriptionRepository.findByUserIdOrderByUploadTimestampDesc(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PrescriptionResponse getPrescription(UUID prescriptionId) {
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));
        return mapToDTO(prescription);
    }
    
    @Override
    public PrescriptionResponse getPrescriptionWithResponses(UUID prescriptionId) {
        Prescription prescription = prescriptionRepository.findByIdWithResponses(prescriptionId);
        if (prescription == null) {
            throw new RuntimeException("Prescription not found");
        }
        return mapToDTOWithResponses(prescription);
    }

    @Override
    @Transactional
    public PrescriptionResponse updatePrescription(UUID prescriptionId, PrescriptionUploadRequest request, UUID authenticatedUserId) {
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));
        
        // Authorization check - only owner can update
        if (!prescription.getUserId().equals(authenticatedUserId)) {
            throw new RuntimeException("Unauthorized: You can only update your own prescriptions");
        }
        
        // Update fields
        prescription.setNotes(request.getNotes());
        prescription = prescriptionRepository.save(prescription);
        
        log.info("Prescription {} updated by user {}", prescriptionId, authenticatedUserId);
        return mapToDTO(prescription);
    }

    @Override
    @Transactional
    public void deletePrescription(UUID prescriptionId, UUID authenticatedUserId) {
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));
        
        // Authorization check - only owner can delete
        if (!prescription.getUserId().equals(authenticatedUserId)) {
            throw new RuntimeException("Unauthorized: You can only delete your own prescriptions");
        }
        
        prescriptionRepository.delete(prescription);
        log.info("Prescription {} deleted by user {}", prescriptionId, authenticatedUserId);
    }
    
    // ======================== Branch Response Operations ========================
    
    @Override
    @Transactional
    public BranchResponseDTO submitBranchResponse(UUID prescriptionId, BranchResponseRequest request) {
        // 1. Validate prescription exists
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));
        
        // 2. Check for existing response from this branch (idempotency)
        if (responseRepository.existsByPrescriptionIdAndBranchId(prescriptionId, request.getBranchId())) {
            throw new RuntimeException("Branch has already responded to this prescription. Use update endpoint instead.");
        }
        
        // 3. Determine response status based on medicine availability
        ResponseStatus status = determineResponseStatus(request.getMedicines());
        
        // 4. Create and save response
        com.lifepill.prescription.entity.PrescriptionResponse response = com.lifepill.prescription.entity.PrescriptionResponse.builder()
                .prescription(prescription)
                .branchId(request.getBranchId())
                .pharmacistId(request.getPharmacistId())
                .status(status)
                .totalAmount(request.getTotalAmount())
                .notes(request.getNotes())
                .responseTimestamp(LocalDateTime.now())
                .build();
        
        response = responseRepository.save(response);
        
        // 5. Save medicine availabilities
        List<MedicineAvailability> medicines = saveMedicineAvailabilities(response, request.getMedicines());
        response.setMedicineAvailabilities(medicines);
        
        // 6. Update prescription status
        prescription.setStatus(Prescription.PrescriptionStatus.RESPONDED);
        prescriptionRepository.save(prescription);
        
        // 7. Publish event for user notification
        PrescriptionResponseEvent event = buildResponseEvent(prescription, response);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY_RESPONSE, event);
        log.info("Published prescription response event for prescription: {}, branch: {}", prescriptionId, request.getBranchId());
        
        return mapToResponseDTO(response);
    }
    
    @Override
    @Transactional
    public BranchResponseDTO updateBranchResponse(UUID prescriptionId, UUID responseId, BranchResponseRequest request) {
        // 1. Validate prescription exists
        prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));
        
        // 2. Find existing response
        com.lifepill.prescription.entity.PrescriptionResponse response = responseRepository.findById(responseId)
                .orElseThrow(() -> new RuntimeException("Response not found"));
        
        // 3. Validate response belongs to this prescription
        if (!response.getPrescription().getId().equals(prescriptionId)) {
            throw new RuntimeException("Response does not belong to this prescription");
        }
        
        // 4. Validate same branch is updating
        if (!response.getBranchId().equals(request.getBranchId())) {
            throw new RuntimeException("Only the original branch can update this response");
        }
        
        // 5. Delete existing medicine availabilities
        medicineAvailabilityRepository.deleteAll(response.getMedicineAvailabilities());
        
        // 6. Update response
        ResponseStatus status = determineResponseStatus(request.getMedicines());
        response.setStatus(status);
        response.setTotalAmount(request.getTotalAmount());
        response.setNotes(request.getNotes());
        response.setPharmacistId(request.getPharmacistId());
        
        response = responseRepository.save(response);
        
        // 7. Save new medicine availabilities
        List<MedicineAvailability> medicines = saveMedicineAvailabilities(response, request.getMedicines());
        response.setMedicineAvailabilities(medicines);
        
        // 8. Publish update event
        PrescriptionResponseEvent event = buildResponseEvent(response.getPrescription(), response);
        event.setEventType("PRESCRIPTION_RESPONSE_UPDATED");
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY_RESPONSE, event);
        log.info("Updated prescription response: {} for prescription: {}", responseId, prescriptionId);
        
        return mapToResponseDTO(response);
    }
    
    @Override
    public List<BranchResponseDTO> getPrescriptionResponses(UUID prescriptionId) {
        // Validate prescription exists
        prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));
        
        return responseRepository.findByPrescriptionIdWithMedicines(prescriptionId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public BranchResponseDTO getResponseById(UUID prescriptionId, UUID responseId) {
        com.lifepill.prescription.entity.PrescriptionResponse response = responseRepository.findById(responseId)
                .orElseThrow(() -> new RuntimeException("Response not found"));
        
        if (!response.getPrescription().getId().equals(prescriptionId)) {
            throw new RuntimeException("Response does not belong to this prescription");
        }
        
        return mapToResponseDTO(response);
    }
    
    // ======================== Helper Methods ========================
    
    private ResponseStatus determineResponseStatus(List<BranchResponseRequest.MedicineAvailabilityRequest> medicines) {
        if (medicines == null || medicines.isEmpty()) {
            return ResponseStatus.REVIEWING;
        }
        
        long availableCount = medicines.stream()
                .filter(m -> Boolean.TRUE.equals(m.getIsAvailable()))
                .count();
        
        if (availableCount == 0) {
            return ResponseStatus.NOT_AVAILABLE;
        } else if (availableCount == medicines.size()) {
            return ResponseStatus.AVAILABLE;
        } else {
            return ResponseStatus.PARTIALLY_AVAILABLE;
        }
    }
    
    private List<MedicineAvailability> saveMedicineAvailabilities(
            com.lifepill.prescription.entity.PrescriptionResponse response,
            List<BranchResponseRequest.MedicineAvailabilityRequest> medicines) {
        
        if (medicines == null || medicines.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<MedicineAvailability> availabilities = medicines.stream()
                .map(med -> MedicineAvailability.builder()
                        .response(response)
                        .medicineName(med.getMedicineName())
                        .itemId(med.getItemId())
                        .itemBarCode(med.getItemBarCode())
                        .measuringUnitType(med.getMeasuringUnitType())
                        .branchId(response.getBranchId() != null ? response.getBranchId().getLeastSignificantBits() : null)
                        .isAvailable(med.getIsAvailable())
                        .stock(med.getStock())
                        .quantityAvailable(med.getQuantityAvailable())
                        .unitPrice(med.getUnitPrice())
                        .notes(med.getNotes())
                        .build())
                .collect(Collectors.toList());
        
        return medicineAvailabilityRepository.saveAll(availabilities);
    }
    
    private PrescriptionResponseEvent buildResponseEvent(Prescription prescription, 
            com.lifepill.prescription.entity.PrescriptionResponse response) {
        
        List<PrescriptionResponseEvent.MedicineInfo> medicineInfos = response.getMedicineAvailabilities().stream()
                .map(med -> PrescriptionResponseEvent.MedicineInfo.builder()
                        .medicineName(med.getMedicineName())
                        .isAvailable(med.getIsAvailable())
                        .quantityAvailable(med.getQuantityAvailable())
                        .unitPrice(med.getUnitPrice())
                        .build())
                .collect(Collectors.toList());
        
        return PrescriptionResponseEvent.builder()
                .responseId(response.getId())
                .prescriptionId(prescription.getId())
                .userId(prescription.getUserId())
                .branchId(response.getBranchId())
                .pharmacistId(response.getPharmacistId())
                .status(response.getStatus().name())
                .totalAmount(response.getTotalAmount())
                .notes(response.getNotes())
                .responseTimestamp(response.getResponseTimestamp())
                .medicines(medicineInfos)
                .build();
    }

    private PrescriptionResponse mapToDTO(Prescription prescription) {
        return PrescriptionResponse.builder()
                .id(prescription.getId())
                .userId(prescription.getUserId())
                .imageUrl(prescription.getImageUrl())
                .notes(prescription.getNotes())
                .status(prescription.getStatus().name())
                .uploadTimestamp(prescription.getUploadTimestamp())
                .totalResponses(prescription.getResponses() != null ? prescription.getResponses().size() : 0)
                .responses(new ArrayList<>()) // Simplified - use getPrescriptionWithResponses for full data
                .build();
    }
    
    private PrescriptionResponse mapToDTOWithResponses(Prescription prescription) {
        List<BranchResponseDTO> responses = prescription.getResponses() != null
                ? prescription.getResponses().stream()
                    .map(this::mapToResponseDTO)
                    .collect(Collectors.toList())
                : new ArrayList<>();
        
        return PrescriptionResponse.builder()
                .id(prescription.getId())
                .userId(prescription.getUserId())
                .imageUrl(prescription.getImageUrl())
                .notes(prescription.getNotes())
                .status(prescription.getStatus().name())
                .uploadTimestamp(prescription.getUploadTimestamp())
                .totalResponses(responses.size())
                .responses(responses)
                .build();
    }
    
    private BranchResponseDTO mapToResponseDTO(com.lifepill.prescription.entity.PrescriptionResponse response) {
        List<MedicineAvailabilityDTO> medicines = response.getMedicineAvailabilities() != null
                ? response.getMedicineAvailabilities().stream()
                    .map(this::mapToMedicineDTO)
                    .collect(Collectors.toList())
                : new ArrayList<>();
        
        return BranchResponseDTO.builder()
                .id(response.getId())
                .branchId(response.getBranchId())
                .pharmacistId(response.getPharmacistId())
                .status(response.getStatus().name())
                .totalAmount(response.getTotalAmount())
                .notes(response.getNotes())
                .responseTimestamp(response.getResponseTimestamp())
                .medicines(medicines)
                .build();
    }
    
    private MedicineAvailabilityDTO mapToMedicineDTO(MedicineAvailability medicine) {
        return MedicineAvailabilityDTO.builder()
                .id(medicine.getId())
                .medicineName(medicine.getMedicineName())
                .itemId(medicine.getItemId())
                .itemBarCode(medicine.getItemBarCode())
                .measuringUnitType(medicine.getMeasuringUnitType())
                .branchId(medicine.getBranchId())
                .isAvailable(medicine.getIsAvailable())
                .stock(medicine.getStock())
                .quantityAvailable(medicine.getQuantityAvailable())
                .unitPrice(medicine.getUnitPrice())
                .notes(medicine.getNotes())
                .build();
    }
    
    // ======================== Order Placement ========================
    
    @Override
    @Transactional
    public com.lifepill.prescription.dto.response.OrderPlacementResponse placeOrder(
            UUID prescriptionId, 
            com.lifepill.prescription.dto.request.PlaceOrderRequest request) {
        
        // 1. Validate prescription exists
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));
        
        // 2. Validate user owns this prescription
        if (!prescription.getUserId().equals(request.getUserId())) {
            throw new RuntimeException("Unauthorized: You can only place orders for your own prescriptions");
        }
        
        // 3. Validate prescription has responses
        if (prescription.getStatus() != Prescription.PrescriptionStatus.RESPONDED) {
            throw new RuntimeException("Cannot place order: Prescription has no responses yet");
        }
        
        // 4. Get the selected response
        com.lifepill.prescription.entity.PrescriptionResponse selectedResponse = responseRepository.findById(request.getResponseId())
                .orElseThrow(() -> new RuntimeException("Selected response not found"));
        
        // 5. Validate response belongs to this prescription
        if (!selectedResponse.getPrescription().getId().equals(prescriptionId)) {
            throw new RuntimeException("Selected response does not belong to this prescription");
        }
        
        // 6. Validate response has available medicines
        if (selectedResponse.getStatus() == ResponseStatus.NOT_AVAILABLE) {
            throw new RuntimeException("Cannot place order: No medicines available in selected response");
        }
        
        // 7. Generate order ID
        UUID orderId = UUID.randomUUID();
        LocalDateTime orderPlacedAt = LocalDateTime.now();
        
        // 8. Update prescription status to ORDER_PLACED
        prescription.setStatus(Prescription.PrescriptionStatus.ORDER_PLACED);
        prescriptionRepository.save(prescription);
        
        // 9. Build order items from medicine availabilities
        List<com.lifepill.prescription.event.OrderCreatedEvent.OrderItemInfo> orderItems = 
                selectedResponse.getMedicineAvailabilities().stream()
                        .filter(med -> Boolean.TRUE.equals(med.getIsAvailable()))
                        .map(med -> com.lifepill.prescription.event.OrderCreatedEvent.OrderItemInfo.builder()
                                .medicineName(med.getMedicineName())
                                .quantity(med.getQuantityAvailable())
                                .unitPrice(med.getUnitPrice())
                                .build())
                        .collect(Collectors.toList());
        
        // 10. Publish order created event
        com.lifepill.prescription.event.OrderCreatedEvent event = com.lifepill.prescription.event.OrderCreatedEvent.builder()
                .orderId(orderId)
                .prescriptionId(prescriptionId)
                .userId(prescription.getUserId())
                .branchId(selectedResponse.getBranchId())
                .responseId(request.getResponseId())
                .totalAmount(selectedResponse.getTotalAmount())
                .deliveryAddress(request.getDeliveryAddress())
                .deliveryNotes(request.getDeliveryNotes())
                .paymentMethod(request.getPaymentMethod())
                .contactPhone(request.getContactPhone())
                .orderPlacedAt(orderPlacedAt)
                .items(orderItems)
                .build();
        
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY_ORDER, event);
        log.info("Published order created event: orderId={}, prescriptionId={}, branchId={}", 
                orderId, prescriptionId, selectedResponse.getBranchId());
        
        // 11. Build response
        List<com.lifepill.prescription.dto.response.OrderPlacementResponse.OrderItem> responseItems = 
                selectedResponse.getMedicineAvailabilities().stream()
                        .filter(med -> Boolean.TRUE.equals(med.getIsAvailable()))
                        .map(med -> com.lifepill.prescription.dto.response.OrderPlacementResponse.OrderItem.builder()
                                .medicineName(med.getMedicineName())
                                .quantity(med.getQuantityAvailable())
                                .unitPrice(med.getUnitPrice())
                                .totalPrice(med.getUnitPrice() != null && med.getQuantityAvailable() != null 
                                        ? med.getUnitPrice().multiply(BigDecimal.valueOf(med.getQuantityAvailable()))
                                        : null)
                                .build())
                        .collect(Collectors.toList());
        
        return com.lifepill.prescription.dto.response.OrderPlacementResponse.builder()
                .orderId(orderId)
                .prescriptionId(prescriptionId)
                .branchId(selectedResponse.getBranchId())
                .userId(prescription.getUserId())
                .status("ORDER_PLACED")
                .totalAmount(selectedResponse.getTotalAmount())
                .deliveryAddress(request.getDeliveryAddress())
                .paymentMethod(request.getPaymentMethod())
                .orderPlacedAt(orderPlacedAt)
                .estimatedDeliveryTime("30-45 minutes")
                .items(responseItems)
                .build();
    }
}

