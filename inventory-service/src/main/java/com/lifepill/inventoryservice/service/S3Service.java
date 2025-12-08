package com.lifepill.inventoryservice.service;

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
 * Service for handling AWS S3 operations in Inventory Service
 * Provides methods to upload, delete, and manage files in S3 bucket
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
     *
     * @param file   MultipartFile to upload
     * @param folder Folder/prefix in S3 bucket (e.g., "items", "suppliers", "categories")
     * @return Full S3 URL of uploaded file
     * @throws IOException if file reading fails
     */
    public String uploadFile(MultipartFile file, String folder) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Cannot upload empty file");
        }

        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";

        // Generate unique filename with UUID to prevent collisions
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
     *
     * @param fileUrl Full S3 URL of the file to delete
     */
    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            log.warn("Cannot delete file: URL is null or empty");
            return;
        }

        try {
            // Extract key from URL
            String key = extractKeyFromUrl(fileUrl);
            if (key == null) {
                log.warn("Could not extract key from URL: {}", fileUrl);
                return;
            }

            log.info("Deleting file from S3: bucket={}, key={}", bucketName, key);

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
            log.info("File deleted successfully from S3: {}", key);

        } catch (S3Exception e) {
            log.error("Failed to delete file from S3: {}", e.getMessage(), e);
            // Don't throw exception, just log the error
        }
    }

    /**
     * Get public URL for a file (for public-read files)
     *
     * @param key S3 object key
     * @return Full S3 URL
     */
    public String getFileUrl(String key) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, key);
    }

    /**
     * Extract S3 key from full URL
     *
     * @param fileUrl Full S3 URL
     * @return S3 object key or null if extraction fails
     */
    private String extractKeyFromUrl(String fileUrl) {
        try {
            // URL format: https://bucket-name.s3.region.amazonaws.com/key
            String[] parts = fileUrl.split(bucketName + ".s3." + region + ".amazonaws.com/");
            if (parts.length > 1) {
                return parts[1];
            }
            
            // Alternative format: https://s3.region.amazonaws.com/bucket-name/key
            parts = fileUrl.split(bucketName + "/");
            if (parts.length > 1) {
                return parts[1];
            }
        } catch (Exception e) {
            log.error("Error extracting key from URL: {}", fileUrl, e);
        }
        return null;
    }

    /**
     * Check if a file exists in S3
     *
     * @param key S3 object key
     * @return true if file exists, false otherwise
     */
    public boolean fileExists(String key) {
        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.headObject(headObjectRequest);
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        } catch (S3Exception e) {
            log.error("Error checking file existence: {}", e.getMessage());
            return false;
        }
    }
}
