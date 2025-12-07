package com.lifepill.identityservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Branch information from Branch Service.
 * Used for inter-service communication via OpenFeign.
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
    private byte[] branchImage;
    private Boolean branchStatus;
    private String branchLocation;
    private String branchProfileImageUrl;
}
