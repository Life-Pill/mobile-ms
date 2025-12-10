package com.lifepill.prescription.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
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
    private Long branchId;
    
    @NotNull(message = "User ID is required")
    private UUID userId; // The prescription owner (mobile user) to notify
    
    /**
     * Response status: AVAILABLE, PARTIALLY_AVAILABLE, NOT_AVAILABLE
     * If not provided, status will be determined from medicine availability.
     */
    private String status;
    
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
        
        @NotBlank(message = "Medicine name is required")
        private String medicineName;
        
        private Long itemId; // Optional: link to inventory item
        private String itemBarCode;
        private String measuringUnitType;
        
        @NotNull(message = "Availability status is required")
        private Boolean isAvailable;
        
        private Boolean stock;
        
        @Min(value = 0, message = "Quantity must be non-negative")
        private Integer quantityAvailable;
        
        @DecimalMin(value = "0.0", message = "Unit price must be non-negative")
        private BigDecimal unitPrice;
        
        private String notes;
    }
}
