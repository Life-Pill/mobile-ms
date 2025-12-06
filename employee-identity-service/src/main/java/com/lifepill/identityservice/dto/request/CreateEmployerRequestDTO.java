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
    private String employerEmail;
    private String employerPassword;
    private String role;
    private Integer pin;
}
