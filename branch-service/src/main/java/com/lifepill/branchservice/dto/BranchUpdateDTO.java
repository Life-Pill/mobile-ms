package com.lifepill.branchservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Branch update DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchUpdateDTO {
    private Long branchId;
    private String branchName;
    private String branchAddress;
    private String branchContact;
    private String branchFax;
    private String branchEmail;
    private String branchDescription;
    private String branchLocation;
    private Double branchLatitude;
    private Double branchLongitude;
}
