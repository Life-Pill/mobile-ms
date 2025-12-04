package com.lifepill.identityservice.dto.request;

import com.lifepill.identityservice.entity.enums.Gender;
import com.lifepill.identityservice.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

/**
 * DTO for employer registration request.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {
    
    private long employerId;
    
    private String employerNicName;
    
    @NotBlank(message = "First name is required")
    private String employerFirstName;
    
    private String employerLastName;
    
    @NotBlank(message = "Password is required")
    private String employerPassword;
    
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String employerEmail;
    
    private String employerPhone;
    
    private String employerAddress;
    
    private double employerSalary;
    
    @NotBlank(message = "NIC is required")
    private String employerNic;
    
    private int pin;
    
    @NotNull(message = "Gender is required")
    private Gender gender;
    
    private Date dateOfBirth;
    
    @NotNull(message = "Role is required")
    private Role role;
    
    @NotNull(message = "Branch ID is required")
    private Long branchId;
}
