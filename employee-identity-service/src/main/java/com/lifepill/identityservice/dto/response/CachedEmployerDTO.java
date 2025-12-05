package com.lifepill.identityservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for cached employer session data stored in Redis.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CachedEmployerDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long employerId;
    private String employerNicName;
    private String employerFirstName;
    private String employerLastName;
    private String employerEmail;
    private String employerPhone;
    private String employerAddress;
    private Double employerSalary;
    private String employerNic;
    private String role;
    private Long branchId;
    private boolean activeStatus;
    private String accessToken;
    private String refreshToken;
    private long loginTimestamp;
    private long lastActivityTimestamp;
    private long expiresAt;
    private boolean revoked;
    private String gender;
    private String dateOfBirth;
    private Integer pin;
}
