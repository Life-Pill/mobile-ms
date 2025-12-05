package com.lifepill.identityservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Response DTO for cached session data matching the expected format.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CachedSessionResponseDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private AuthenticationResponsePart authenticationResponse;
    private EmployerDetailsPart employerDetails;
    private boolean revoked;
    private long expiresAt;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthenticationResponsePart implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private String message;
        
        @JsonProperty("access_token")
        private String accessToken;
        
        @JsonProperty("refresh_token")
        private String refreshToken;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmployerDetailsPart implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private Long employerId;
        private Long branchId;
        private String employerNicName;
        private String employerFirstName;
        private String employerLastName;
        private String employerEmail;
        private String employerPhone;
        private String employerAddress;
        private Double employerSalary;
        private String employerNic;
        private String gender;
        private String dateOfBirth;
        private String role;
        private Integer pin;
        private String profileImage;
        private boolean activeStatus;
    }
}
