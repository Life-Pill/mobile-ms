package com.lifepill.identityservice.dto.request;

import com.lifepill.identityservice.entity.enums.Gender;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * DTO for updating employer account details (personal information).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateEmployerAccountDTO {
    
    @NotNull(message = "Employer ID is required")
    private Long employerId;
    
    @NotNull(message = "First name is required")
    private String employerFirstName;
    
    private String employerLastName;
    
    @NotNull(message = "Gender is required")
    private Gender gender;
    
    private String employerAddress;
    
    private Date dateOfBirth;
}
