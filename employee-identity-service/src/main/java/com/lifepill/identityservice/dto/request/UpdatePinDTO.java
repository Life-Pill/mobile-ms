package com.lifepill.identityservice.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating employer PIN.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePinDTO {
    
    @NotNull(message = "Employer ID is required")
    private Long employerId;
    
    @NotNull(message = "PIN is required")
    @Min(value = 1000, message = "PIN must be 4 digits")
    @Max(value = 9999, message = "PIN must be 4 digits")
    private Integer pin;
}
