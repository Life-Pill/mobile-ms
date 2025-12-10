package com.lifepill.notification.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for prescription notification sent via WebSocket to POS systems.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionNotificationDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private UUID prescriptionId;
    private UUID userId;
    private String userName;
    private String imageUrl;
    private String notes;
    private String status;
    private LocalDateTime uploadTimestamp;
    private String eventType;
    
    // Notification metadata
    private LocalDateTime notificationTimestamp;
    private String priority; // HIGH, NORMAL, LOW
}
