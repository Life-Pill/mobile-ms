package com.lifepill.notification.service;

import com.lifepill.notification.dto.BranchResponseNotificationDTO;
import com.lifepill.notification.dto.PrescriptionNotificationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    
    // WebSocket topic destinations
    private static final String TOPIC_ALL_PRESCRIPTIONS = "/topic/prescriptions";
    private static final String TOPIC_BRANCH_PRESCRIPTIONS = "/topic/branch/%s/prescriptions";
    private static final String TOPIC_USER_RESPONSES = "/topic/user/%s/responses";
    private static final String TOPIC_USER_NOTIFICATIONS = "/topic/user/%s/notifications";

    /**
     * Broadcast prescription upload to all connected POS systems.
     * This is the fan-out pattern where all branches receive the notification.
     */
    public void broadcastPrescriptionUpload(Object prescriptionData) {
        log.info("Broadcasting prescription upload to all branches: {}", prescriptionData);
        messagingTemplate.convertAndSend(TOPIC_ALL_PRESCRIPTIONS, prescriptionData);
    }
    
    /**
     * Broadcast prescription notification with transformed DTO.
     */
    public void broadcastPrescriptionNotification(PrescriptionNotificationDTO notification) {
        notification.setNotificationTimestamp(LocalDateTime.now());
        notification.setPriority("NORMAL");
        
        log.info("Broadcasting prescription notification to all branches: prescriptionId={}", 
                notification.getPrescriptionId());
        messagingTemplate.convertAndSend(TOPIC_ALL_PRESCRIPTIONS, notification);
    }
    
    /**
     * Send notification to a specific branch.
     * Useful for targeted notifications or follow-ups.
     */
    public void sendToBranch(UUID branchId, Object data) {
        String destination = String.format(TOPIC_BRANCH_PRESCRIPTIONS, branchId.toString());
        log.info("Sending notification to branch {}: {}", branchId, data);
        messagingTemplate.convertAndSend(destination, data);
    }
    
    /**
     * Notify a specific user about branch responses.
     * This is used when a branch submits a response to the user's prescription.
     */
    public void notifyUser(UUID userId, Object data) {
        String destination = String.format(TOPIC_USER_RESPONSES, userId.toString());
        log.info("Sending response notification to user {}", userId);
        messagingTemplate.convertAndSend(destination, data);
    }
    
    /**
     * Send branch response notification to user with full DTO.
     */
    public void sendBranchResponseToUser(BranchResponseNotificationDTO notification) {
        notification.setNotificationTimestamp(LocalDateTime.now());
        
        String destination = String.format(TOPIC_USER_RESPONSES, notification.getPrescriptionId().toString());
        log.info("Sending branch response notification to user for prescription {}: branch={}, status={}", 
                notification.getPrescriptionId(), notification.getBranchId(), notification.getStatus());
        
        // Send to user-specific topic based on userId (extracted from prescription)
        messagingTemplate.convertAndSend(destination, notification);
    }
    
    /**
     * Send general notification to a specific user.
     */
    public void sendNotificationToUser(UUID userId, Object notification) {
        String destination = String.format(TOPIC_USER_NOTIFICATIONS, userId.toString());
        log.info("Sending general notification to user {}", userId);
        messagingTemplate.convertAndSend(destination, notification);
    }
    
    /**
     * Broadcast to all branches (alias for broadcastPrescriptionUpload).
     */
    public void broadcastToAllBranches(Object data) {
        log.info("Broadcasting to all branches");
        messagingTemplate.convertAndSend(TOPIC_ALL_PRESCRIPTIONS, data);
    }
}
