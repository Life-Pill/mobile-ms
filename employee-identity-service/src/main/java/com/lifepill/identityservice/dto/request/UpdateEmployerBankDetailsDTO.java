package com.lifepill.identityservice.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating employer bank account details.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateEmployerBankDetailsDTO {
    
    private Long employerBankDetailsId;
    
    @NotNull(message = "Bank name is required")
    private String bankName;
    
    private String bankBranchName;
    
    @NotNull(message = "Bank account number is required")
    private String bankAccountNumber;
    
    private String employerDescription;
    
    @Positive(message = "Monthly payment must be positive")
    private Double monthlyPayment;
    
    private Boolean monthlyPaymentStatus;
    
    @NotNull(message = "Employer ID is required")
    private Long employerId;
}
