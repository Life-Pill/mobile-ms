package com.lifepill.possystem.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Branch data from Branch Service
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MicroserviceBranchDTO {
    private Long branchId;
    private String branchName;
    private String branchAddress;
    private String branchContact;
    private String branchEmail;
    private String branchDescription;
    private boolean branchStatus;
    private Double branchLatitude;
    private Double branchLongitude;
}
