package com.lifepill.identityservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for employer bank details.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployerBankDetailsDTO {
    
    private long employerBankDetailsId;
    
    private String bankName;
    
    private String bankBranchName;
    
    private String bankAccountNumber;
    
    private String employerDescription;
    
    private double monthlyPayment;
    
    private Boolean monthlyPaymentStatus;
    
    private long employerId;
}
