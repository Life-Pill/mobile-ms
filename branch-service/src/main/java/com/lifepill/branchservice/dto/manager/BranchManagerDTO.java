package com.lifepill.branchservice.dto.manager;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Branch Manager data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchManagerDTO {
    private Long employerId;
    private Long branchId;
    private String employerNicName;
    private String employerFirstName;
    private String employerLastName;
    private String employerPassword;
    private String employerEmail;
    private String employerPhone;
    private String employerAddress;
    private Double employerSalary;
    private String employerNic;
    private String gender;
    private String dateOfBirth;
    private String role;
    private Integer pin;
    private String profileImage;
    private boolean activeStatus;
}
