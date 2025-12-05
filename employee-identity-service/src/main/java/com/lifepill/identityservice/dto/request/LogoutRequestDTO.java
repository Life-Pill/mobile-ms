package com.lifepill.identityservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for logout operations.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogoutRequestDTO {
    
    @NotBlank(message = "Username (email) is required")
    private String username;
}
