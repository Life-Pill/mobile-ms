package com.lifepill.notification.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO for branch response notification sent to mobile users.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchResponseNotificationDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private UUID responseId;
    private UUID prescriptionId;
    private UUID branchId;
    private String branchName;
    private UUID pharmacistId;
    private String pharmacistName;
    private String status; // AVAILABLE, PARTIALLY_AVAILABLE, NOT_AVAILABLE
    private BigDecimal totalAmount;
    private String notes;
    private LocalDateTime responseTimestamp;
    private String eventType;
    private List<MedicineInfo> medicines;
    
    // Notification metadata
    private LocalDateTime notificationTimestamp;
    
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
