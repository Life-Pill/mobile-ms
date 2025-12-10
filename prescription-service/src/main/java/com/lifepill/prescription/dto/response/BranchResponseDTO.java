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
    private Long branchId;
    private String branchName;
    private String branchAddress;
    private String branchContact;
    private String branchEmail;
    private Double branchLatitude;
    private Double branchLongitude;
    private String branchLocation;
    private UUID employerId;
    private String status;
    private BigDecimal totalAmount;
    private String notes;
    private LocalDateTime responseTimestamp;
    private List<MedicineAvailabilityDTO> medicines;
}

