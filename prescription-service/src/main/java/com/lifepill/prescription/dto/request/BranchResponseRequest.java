package com.lifepill.prescription.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchResponseRequest {
    
    @NotNull(message = "Branch ID is required")
    private UUID branchId;
    
    @NotNull(message = "Pharmacist ID is required")
    private UUID pharmacistId;
    
    @NotEmpty(message = "At least one medicine must be specified")
    @Valid
    private List<MedicineAvailabilityRequest> medicines;
    
    @PositiveOrZero(message = "Total amount must be positive or zero")
    private BigDecimal totalAmount;
    
    private String notes;
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MedicineAvailabilityRequest {
        
        @NotNull(message = "Medicine name is required")
        private String medicineName;
        
        @NotNull(message = "Availability status is required")
        private Boolean isAvailable;
        
        @PositiveOrZero(message = "Quantity must be positive or zero")
        private Integer quantityAvailable;
        
        @PositiveOrZero(message = "Unit price must be positive or zero")
        private BigDecimal unitPrice;
        
        private String notes;
    }
}
