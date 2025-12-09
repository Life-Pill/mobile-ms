package com.lifepill.prescription.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineAvailabilityDTO {
    
    private UUID id;
    private String medicineName;
    private Long itemId; // Link to inventory item
    private String itemBarCode;
    private String measuringUnitType; // TABLETS, CAPSULES, ML, etc.
    private Long branchId;
    private String branchName; // Can be populated from branch data
    private Boolean isAvailable;
    private Boolean stock; // In stock status
    private Integer quantityAvailable;
    private BigDecimal unitPrice;
    private String notes;
}
