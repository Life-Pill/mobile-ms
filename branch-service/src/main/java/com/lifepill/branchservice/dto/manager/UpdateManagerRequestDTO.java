package com.lifepill.branchservice.dto.manager;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for updating or creating a manager.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateManagerRequestDTO {
    private String employerFirstName;
    private String employerLastName;
    private String employerEmail;
    private String employerPassword;
    private String role;
    private Integer pin;
}
