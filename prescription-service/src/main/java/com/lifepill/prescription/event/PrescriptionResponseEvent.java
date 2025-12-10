package com.lifepill.prescription.event;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Event DTO published when a branch responds to a prescription.
 * This event is consumed by the Notification Service to notify the mobile user.
 * Includes full branch details for mobile app display.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionResponseEvent implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private UUID responseId;
    private UUID prescriptionId;
    private UUID userId; // The prescription owner to notify
    
    // Branch identification
    private Long branchId;
    private String branchName;
    
    // Branch details for mobile app
    private String branchAddress;
    private String branchContact;
    private String branchEmail;
    private Double branchLatitude;
    private Double branchLongitude;
    private String branchLocation;
    
    // Employer info (POS user who responded)
    private UUID employerId;
    private String employerName;
    
    // Response details
    private String status; // AVAILABLE, PARTIALLY_AVAILABLE, NOT_AVAILABLE
    private BigDecimal totalAmount;
    private String notes;
    private LocalDateTime responseTimestamp;
    private List<MedicineInfo> medicines;
    
    // Type identifier for message routing
    @Builder.Default
    private String eventType = "PRESCRIPTION_RESPONSE";
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MedicineInfo implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private String medicineName;
        private Boolean isAvailable;
        private Integer quantityAvailable;
        private BigDecimal unitPrice;
    }
}

