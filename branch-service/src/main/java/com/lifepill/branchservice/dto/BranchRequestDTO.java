package com.lifepill.branchservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Branch request DTO for creating/updating branches
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchRequestDTO {
    private String branchName;
    private String branchAddress;
    private String branchContact;
    private String branchFax;
    private String branchEmail;
    private String branchDescription;
    private String branchLocation;
    private String branchCreatedBy;
    private Double branchLatitude;
    private Double branchLongitude;
}
