package com.lifepill.identityservice.dto;

import com.lifepill.identityservice.entity.enums.Gender;
import com.lifepill.identityservice.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * DTO for employer with complete bank details information.
 * Used for responses that include both employer and bank account data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployerWithBankDetailsDTO {
    
    private Long employerId;
    private Long branchId;
    private String employerNicName;
    private String employerFirstName;
    private String employerLastName;
    private String employerEmail;
    private String employerPhone;
    private String employerAddress;
    private Double employerSalary;
    private String employerNic;
    private Gender gender;
    private Date dateOfBirth;
    private Role role;
    private Integer pin;
    private byte[] profileImage;
    private String profileImageUrl;
    private Boolean activeStatus;
    
    // Nested bank details
    private EmployerBankDetailsDTO employerBankDetails;
}
