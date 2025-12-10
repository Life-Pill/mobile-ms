package com.lifepill.notification.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO for branch response notification sent to mobile users.
 * Includes full branch details for displaying in mobile app.
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
    private UUID userId; // The prescription owner to notify
    
    // Branch identification
    private Long branchId;
    private String branchName;
    
    // Branch details for mobile app display
    private String branchAddress;
    private String branchContact;
    private String branchEmail;
    private Double branchLatitude;
    private Double branchLongitude;
    private String branchLocation;
    
    // Note: userId above is the prescription owner who receives notifications
    
    // Response details
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

