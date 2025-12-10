package com.lifepill.notification.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lifepill.notification.dto.BranchResponseNotificationDTO;
import com.lifepill.notification.dto.PrescriptionNotificationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * Redis message subscriber that forwards messages received via Redis pub/sub to WebSocket clients.
 * This enables horizontal scaling of the notification service.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RedisMessageSubscriber {

    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    /**
     * Handle prescription notification messages from Redis.
     * Broadcasts to all connected POS systems.
     */
    public void handlePrescriptionMessage(String message) {
        try {
            log.debug("Received prescription notification from Redis: {}", message);
            PrescriptionNotificationDTO notification = objectMapper.readValue(message, PrescriptionNotificationDTO.class);
            messagingTemplate.convertAndSend("/topic/prescriptions", notification);
            log.info("Broadcasted prescription {} to all WebSocket clients", notification.getPrescriptionId());
        } catch (Exception e) {
            log.error("Error processing prescription message from Redis: {}", e.getMessage(), e);
        }
    }

    /**
     * Handle user notification messages from Redis.
     * Sends to specific user's WebSocket connection.
     */
    public void handleUserMessage(String message) {
        try {
            log.debug("Received user notification from Redis: {}", message);
            BranchResponseNotificationDTO notification = objectMapper.readValue(message, BranchResponseNotificationDTO.class);
            
            // Send to user-specific topic
            String destination = String.format("/topic/user/%s/responses", notification.getPrescriptionId());
            messagingTemplate.convertAndSend(destination, notification);
            log.info("Sent notification to user WebSocket: {}", destination);
        } catch (Exception e) {
            log.error("Error processing user message from Redis: {}", e.getMessage(), e);
        }
    }
}
