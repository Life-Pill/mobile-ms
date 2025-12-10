package com.lifepill.prescription.event;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Event DTO published when a new prescription is uploaded.
 * This event is consumed by the Notification Service to broadcast to all POS systems.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionUploadedEvent implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private UUID prescriptionId;
    private UUID userId;
    private String userName;
    private String imageUrl;
    private String notes;
    private String status;
    private LocalDateTime uploadTimestamp;
    
    // Type identifier for message routing
    @Builder.Default
    private String eventType = "PRESCRIPTION_UPLOADED";
}
