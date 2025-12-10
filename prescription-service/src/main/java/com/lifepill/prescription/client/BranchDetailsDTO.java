package com.lifepill.prescription.client;

import lombok.*;

/**
 * DTO for branch details fetched from Branch Service.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchDetailsDTO {
    
    private Long branchId;
    private String branchName;
    private String branchAddress;
    private String branchContact;
    private String branchFax;
    private String branchEmail;
    private String branchDescription;
    private boolean branchStatus;
    private String branchLocation;
    private Double branchLatitude;
    private Double branchLongitude;
    private String openingHours;
    private String closingHours;
}
