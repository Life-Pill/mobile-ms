package com.lifepill.prescription.service.impl;

import com.lifepill.prescription.config.RabbitMQConfig;
import com.lifepill.prescription.dto.request.PrescriptionUploadRequest;
import com.lifepill.prescription.dto.response.PrescriptionResponse;
import com.lifepill.prescription.entity.Prescription;
import com.lifepill.prescription.repository.PrescriptionRepository;
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

        // 3. Publish Event
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY_UPLOADED, prescription);

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

    private PrescriptionResponse mapToDTO(Prescription prescription) {
        return PrescriptionResponse.builder()
                .id(prescription.getId())
                .userId(prescription.getUserId())
                .imageUrl(prescription.getImageUrl())
                .notes(prescription.getNotes())
                .status(prescription.getStatus().name())
                .uploadTimestamp(prescription.getUploadTimestamp())
                .totalResponses(prescription.getResponses() != null ? prescription.getResponses().size() : 0)
                .responses(new ArrayList<>()) // Simplified for now
                .build();
    }
}
