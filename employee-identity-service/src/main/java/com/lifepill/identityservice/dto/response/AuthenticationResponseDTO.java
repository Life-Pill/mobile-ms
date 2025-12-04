package com.lifepill.identityservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for authentication response.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationResponseDTO {
    
    private String accessToken;
    
    private String refreshToken;
    
    private String message;
    
    private Long employerId;
    
    private String employerEmail;
    
    private String role;
}
