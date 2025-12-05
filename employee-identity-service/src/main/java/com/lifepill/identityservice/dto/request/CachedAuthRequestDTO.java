package com.lifepill.identityservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for cached session authentication.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CachedAuthRequestDTO {
    
    @NotBlank(message = "Username (email) is required")
    private String username;
    
    @NotNull(message = "PIN is required")
    private Integer pin;
}
