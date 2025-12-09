package com.lifepill.identityservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for creating new employer.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateEmployerRequestDTO {
    private String employerFirstName;
    private String employerLastName;
    private String employerNicName;
    private String employerEmail;
    private String employerPassword;
    private String employerPhone;
    private String employerAddress;
    private Double employerSalary;
    private String employerNic;
    private String gender;
    private String dateOfBirth; // Will be parsed to Date in service layer
    private String role;
    private Integer pin;
}
