package com.lifepill.branchservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Branch DTO for API responses
 * Note: Employers are now managed by Identity Service
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchDTO {
    private Long branchId;
    private String branchName;
    private String branchAddress;
    private String branchContact;
    private String branchFax;
    private String branchEmail;
    private String branchDescription;
    private String branchImage;
    private byte[] branchImageData;
    private boolean branchStatus;
    private String branchLocation;
    private String branchCreatedOn;
    private String branchCreatedBy;
    private Double branchLatitude;
    private Double branchLongitude;
}
