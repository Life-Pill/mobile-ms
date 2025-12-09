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
    private Boolean isAvailable;
    private Integer quantityAvailable;
    private BigDecimal unitPrice;
    private String notes;
}
