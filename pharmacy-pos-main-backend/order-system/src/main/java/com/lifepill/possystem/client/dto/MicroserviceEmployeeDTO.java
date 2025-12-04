package com.lifepill.possystem.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Employee data from Identity Service
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MicroserviceEmployeeDTO {
    private Long employerId;
    private String employerFirstName;
    private String employerLastName;
    private String employerEmail;
    private String employerPassword;
    private String employerPhone;
    private Long branchId;
    private String role;
    private boolean activeStatus;
}
