package com.lifepill.possystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Branch data - used for branch summary responses.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchDTO {
    private Long branchId;
    private String branchName;
    private String branchAddress;
    private String branchContact;
    private String branchFax;
    private String branchEmail;
    private String branchDescription;
    private byte[] branchImage;
    private boolean branchStatus;
    private String branchLocation;
    private String branchCreatedOn;
    private String branchCreatedBy;
}
