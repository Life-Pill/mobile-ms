package com.lifepill.identityservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for PIN authentication request.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PinAuthenticationRequestDTO {
    
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String employerEmail;
    
    @NotNull(message = "PIN is required")
    private Integer pin;
}
