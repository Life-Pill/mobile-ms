package com.lifepill.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lifepill.notification.config.RedisConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Service for publishing notifications via Redis pub/sub.
 * Enables horizontal scaling of WebSocket connections across multiple instances.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RedisPublisherService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    /**
     * Publish a prescription notification to all instances via Redis.
     */
    public void publishPrescriptionNotification(Object notification) {
        try {
            String message = objectMapper.writeValueAsString(notification);
            redisTemplate.convertAndSend(RedisConfig.CHANNEL_PRESCRIPTION_NOTIFICATIONS, message);
            log.debug("Published prescription notification to Redis channel");
        } catch (JsonProcessingException e) {
            log.error("Error serializing prescription notification: {}", e.getMessage());
        }
    }

    /**
     * Publish a user notification to all instances via Redis.
     */
    public void publishUserNotification(Object notification) {
        try {
            String message = objectMapper.writeValueAsString(notification);
            redisTemplate.convertAndSend(RedisConfig.CHANNEL_USER_NOTIFICATIONS, message);
            log.debug("Published user notification to Redis channel");
        } catch (JsonProcessingException e) {
            log.error("Error serializing user notification: {}", e.getMessage());
        }
    }

    /**
     * Publish a branch-specific notification to all instances via Redis.
     */
    public void publishBranchNotification(Object notification) {
        try {
            String message = objectMapper.writeValueAsString(notification);
            redisTemplate.convertAndSend(RedisConfig.CHANNEL_BRANCH_NOTIFICATIONS, message);
            log.debug("Published branch notification to Redis channel");
        } catch (JsonProcessingException e) {
            log.error("Error serializing branch notification: {}", e.getMessage());
        }
    }
}
