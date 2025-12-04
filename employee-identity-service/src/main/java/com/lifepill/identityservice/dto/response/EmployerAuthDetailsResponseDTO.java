package com.lifepill.identityservice.dto.response;

import com.lifepill.identityservice.entity.enums.Gender;
import com.lifepill.identityservice.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * DTO for employer authentication details response.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployerAuthDetailsResponseDTO {
    
    private long employerId;
    
    private String employerNicName;
    
    private String employerFirstName;
    
    private String employerLastName;
    
    private String profileImageUrl;
    
    private String employerEmail;
    
    private String employerPhone;
    
    private String employerAddress;
    
    private double employerSalary;
    
    private String employerNic;
    
    private boolean activeStatus;
    
    private int pin;
    
    private Gender gender;
    
    private Date dateOfBirth;
    
    private Role role;
    
    private Long branchId;
}
