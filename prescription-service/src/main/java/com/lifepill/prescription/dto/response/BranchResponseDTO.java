package com.lifepill.prescription.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchResponseDTO {
    
    private UUID id;
    private UUID branchId;
    private UUID pharmacistId;
    private String status;
    private BigDecimal totalAmount;
    private String notes;
    private LocalDateTime responseTimestamp;
    private List<MedicineAvailabilityDTO> medicines;
}
