package com.lifepill.notification.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lifepill.notification.config.RabbitMQConfig;
import com.lifepill.notification.dto.BranchResponseNotificationDTO;
import com.lifepill.notification.dto.PrescriptionNotificationDTO;
import com.lifepill.notification.entity.Notification;
import com.lifepill.notification.service.FcmService;
import com.lifepill.notification.service.NotificationPersistenceService;
import com.lifepill.notification.service.NotificationService;
import com.lifepill.notification.service.RedisPublisherService;
import com.lifepill.notification.service.UserDeviceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class PrescriptionEventListener {

    private final NotificationService notificationService;
    private final NotificationPersistenceService persistenceService;
    private final FcmService fcmService;
    private final RedisPublisherService redisPublisherService;
    private final UserDeviceService userDeviceService;
    private final ObjectMapper objectMapper;

    /**
     * Handles prescription upload events.
     * Broadcasts to all connected POS systems so they can view and respond.
     */
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NOTIFICATION)
    public void handlePrescriptionUploaded(Map<String, Object> eventData) {
        log.info("Received prescription upload event: {}", eventData);
        
        try {
            // Transform to notification DTO
            PrescriptionNotificationDTO notification = PrescriptionNotificationDTO.builder()
                    .prescriptionId(parseUUID(eventData.get("prescriptionId")))
                    .userId(parseUUID(eventData.get("userId")))
                    .userName((String) eventData.get("userName"))
                    .imageUrl((String) eventData.get("imageUrl"))
                    .notes((String) eventData.get("notes"))
                    .status((String) eventData.get("status"))
                    .eventType((String) eventData.getOrDefault("eventType", "PRESCRIPTION_UPLOADED"))
                    .build();

            // Broadcast to all POS systems via WebSocket
            // notificationService.broadcastPrescriptionNotification(notification);
            
            // Publish to Redis for multi-instance support
            // Redis subscriber will broadcast to all WebSocket clients
            // NOTE: Only use Redis to avoid duplicate notifications
            redisPublisherService.publishPrescriptionNotification(notification);
            
            log.info("Successfully published prescription {} to Redis for broadcasting", 
                    notification.getPrescriptionId());
            
        } catch (Exception e) {
            log.error("Error processing prescription upload event: {}", e.getMessage(), e);
        }
    }

    /**
     * Handles prescription response events from branches.
     * Notifies the prescription owner (mobile user) about the response.
     */
    @RabbitListener(queues = RabbitMQConfig.QUEUE_USER_NOTIFICATION)
    public void handlePrescriptionResponse(Map<String, Object> eventData) {
        log.info("Received prescription response event: {}", eventData);
        
        try {
            // Extract user ID from the event (this is the prescription owner to notify)
            UUID userId = parseUUID(eventData.get("userId"));
            UUID prescriptionId = parseUUID(eventData.get("prescriptionId"));
            
            // Parse branchId - can be Long or UUID depending on source
            Long branchId = parseLong(eventData.get("branchId"));
            
            // Transform to notification DTO with full branch details
            BranchResponseNotificationDTO notification = BranchResponseNotificationDTO.builder()
                    .responseId(parseUUID(eventData.get("responseId")))
                    .prescriptionId(prescriptionId)
                    .userId(userId)
                    .branchId(branchId)
                    .branchName((String) eventData.get("branchName"))
                    .branchAddress((String) eventData.get("branchAddress"))
                    .branchContact((String) eventData.get("branchContact"))
                    .branchEmail((String) eventData.get("branchEmail"))
                    .branchLatitude(parseDouble(eventData.get("branchLatitude")))
                    .branchLongitude(parseDouble(eventData.get("branchLongitude")))
                    .branchLocation((String) eventData.get("branchLocation"))
                    .employerId(parseUUID(eventData.get("employerId")))
                    .employerName((String) eventData.get("employerName"))
                    .status((String) eventData.get("status"))
                    .notes((String) eventData.get("notes"))
                    .eventType((String) eventData.getOrDefault("eventType", "PRESCRIPTION_RESPONSE"))
                    .notificationTimestamp(java.time.LocalDateTime.now())
                    .build();
            
            // Parse total amount if present
            Object totalAmount = eventData.get("totalAmount");
            if (totalAmount != null) {
                if (totalAmount instanceof Number) {
                    notification.setTotalAmount(new java.math.BigDecimal(totalAmount.toString()));
                }
            }
            
            if (userId != null) {
                // 1. Save notification to database for history
                persistenceService.saveNotification(
                        userId,
                        Notification.NotificationType.PRESCRIPTION_RESPONSE,
                        "Prescription Response Received",
                        String.format("%s has responded to your prescription: %s", 
                                notification.getBranchName() != null ? notification.getBranchName() : "A pharmacy",
                                notification.getStatus()),
                        notification,
                        prescriptionId,
                        "PRESCRIPTION"
                );
                
                // 2. Notify the user via Redis (which will broadcast to WebSocket)
                redisPublisherService.publishUserNotification(notification);
                log.info("Successfully published user {} notification about response from branch {}", 
                        userId, notification.getBranchId());
                
                // 3. Send FCM push notification (for offline users)
                List<String> fcmTokens = userDeviceService.getFcmTokensForUser(userId);
                if (!fcmTokens.isEmpty()) {
                    for (String fcmToken : fcmTokens) {
                        fcmService.sendPrescriptionResponseNotification(
                                fcmToken, 
                                prescriptionId, 
                                notification.getBranchName(), 
                                notification.getStatus()
                        );
                    }
                    log.info("Sent FCM push notifications to {} devices for user {}", fcmTokens.size(), userId);
                }
                
            } else {
                log.warn("Cannot notify user - userId is null for prescription {}", prescriptionId);
            }
            
        } catch (Exception e) {
            log.error("Error processing prescription response event: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Safely parse Long from various input types.
     */
    private Long parseLong(Object value) {
        if (value == null) return null;
        if (value instanceof Long) return (Long) value;
        if (value instanceof Number) return ((Number) value).longValue();
        if (value instanceof String) {
            try {
                return Long.parseLong((String) value);
            } catch (NumberFormatException e) {
                log.warn("Invalid Long string: {}", value);
                return null;
            }
        }
        return null;
    }
    
    /**
     * Safely parse Double from various input types.
     */
    private Double parseDouble(Object value) {
        if (value == null) return null;
        if (value instanceof Double) return (Double) value;
        if (value instanceof Number) return ((Number) value).doubleValue();
        if (value instanceof String) {
            try {
                return Double.parseDouble((String) value);
            } catch (NumberFormatException e) {
                log.warn("Invalid Double string: {}", value);
                return null;
            }
        }
        return null;
    }
    
    /**
     * Safely parse UUID from various input types.
     */
    private UUID parseUUID(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof UUID) {
            return (UUID) value;
        }
        if (value instanceof String) {
            try {
                return UUID.fromString((String) value);
            } catch (IllegalArgumentException e) {
                log.warn("Invalid UUID string: {}", value);
                return null;
            }
        }
        return null;
    }
}

