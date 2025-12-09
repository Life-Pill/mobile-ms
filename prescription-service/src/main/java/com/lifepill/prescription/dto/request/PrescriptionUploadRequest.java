package com.lifepill.prescription.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionUploadRequest {
    
    @NotNull(message = "User ID is required")
    private UUID userId;
    
    private String notes;
}
