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
    private UUID branchId;
    private String branchName;
    private UUID pharmacistId;
    private String pharmacistName;
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
