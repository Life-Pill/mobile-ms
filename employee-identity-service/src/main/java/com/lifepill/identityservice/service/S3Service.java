package com.lifepill.identityservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.UUID;

/**
 * Service for handling AWS S3 operations in Employee Identity Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${aws.region}")
    private String region;

    /**
     * Upload a file to S3 bucket
     */
    public String uploadFile(MultipartFile file, String folder) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Cannot upload empty file");
        }

        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";

        String uniqueFileName = folder + "/" + UUID.randomUUID() + "-" + System.currentTimeMillis() + fileExtension;

        log.info("Uploading file to S3: bucket={}, key={}", bucketName, uniqueFileName);

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(uniqueFileName)
                    .contentType(file.getContentType())
                    // Removed ACL - bucket uses bucket policy for public access
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

            String fileUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, uniqueFileName);
            log.info("File uploaded successfully to S3: {}", fileUrl);

            return fileUrl;
        } catch (S3Exception e) {
            log.error("Failed to upload file to S3: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to upload file to S3: " + e.awsErrorDetails().errorMessage(), e);
        }
    }

    /**
     * Delete a file from S3 bucket
     */
    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return;
        }

        try {
            String key = extractKeyFromUrl(fileUrl);
            if (key == null) {
                log.warn("Could not extract key from URL: {}", fileUrl);
                return;
            }

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
            log.info("File deleted successfully from S3: {}", key);

        } catch (S3Exception e) {
            log.error("Failed to delete file from S3: {}", e.getMessage(), e);
        }
    }

    private String extractKeyFromUrl(String fileUrl) {
        try {
            String[] parts = fileUrl.split(bucketName + ".s3." + region + ".amazonaws.com/");
            if (parts.length > 1) {
                return parts[1];
            }
            parts = fileUrl.split(bucketName + "/");
            if (parts.length > 1) {
                return parts[1];
            }
        } catch (Exception e) {
            log.error("Error extracting key from URL: {}", fileUrl, e);
        }
        return null;
    }
}
