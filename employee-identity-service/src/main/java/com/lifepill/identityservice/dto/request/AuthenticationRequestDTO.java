package com.lifepill.identityservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO for authentication request.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequestDTO {
    
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String employerEmail;
    
    @NotBlank(message = "Password is required")
    private String employerPassword;
}
