package com.lifepill.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lifepill.notification.entity.Notification;
import com.lifepill.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Service for persisting and retrieving notifications from the database.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationPersistenceService {

    private final NotificationRepository notificationRepository;
    private final ObjectMapper objectMapper;

    /**
     * Save a notification to the database.
     */
    @Transactional
    public Notification saveNotification(UUID userId, Notification.NotificationType type,
                                         String title, String message, Object data,
                                         UUID referenceId, String referenceType) {
        String jsonData = null;
        if (data != null) {
            try {
                jsonData = objectMapper.writeValueAsString(data);
            } catch (JsonProcessingException e) {
                log.warn("Failed to serialize notification data: {}", e.getMessage());
            }
        }

        Notification notification = Notification.builder()
                .userId(userId)
                .type(type)
                .title(title)
                .message(message)
                .data(jsonData)
                .referenceId(referenceId)
                .referenceType(referenceType)
                .isRead(false)
                .isPushed(false)
                .build();

        notification = notificationRepository.save(notification);
        log.debug("Saved notification {} for user {}", notification.getId(), userId);
        return notification;
    }

    /**
     * Get paginated notifications for a user.
     */
    public Page<Notification> getUserNotifications(UUID userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    /**
     * Get unread notifications for a user.
     */
    public List<Notification> getUnreadNotifications(UUID userId) {
        return notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);
    }

    /**
     * Get notification counts for a user.
     */
    public Map<String, Long> getNotificationCounts(UUID userId) {
        long unreadCount = notificationRepository.countByUserIdAndIsReadFalse(userId);
        long totalCount = notificationRepository.countByUserId(userId);
        return Map.of(
                "unreadCount", unreadCount,
                "totalCount", totalCount
        );
    }

    /**
     * Mark a single notification as read.
     */
    @Transactional
    public boolean markAsRead(UUID notificationId) {
        return notificationRepository.findById(notificationId)
                .map(notification -> {
                    if (!notification.getIsRead()) {
                        notification.setIsRead(true);
                        notification.setReadAt(LocalDateTime.now());
                        notificationRepository.save(notification);
                        log.debug("Marked notification {} as read", notificationId);
                    }
                    return true;
                })
                .orElse(false);
    }

    /**
     * Mark all notifications as read for a user.
     */
    @Transactional
    public int markAllAsRead(UUID userId) {
        int count = notificationRepository.markAllAsRead(userId, LocalDateTime.now());
        log.debug("Marked {} notifications as read for user {}", count, userId);
        return count;
    }

    /**
     * Mark notification as pushed (for FCM).
     */
    @Transactional
    public void markAsPushed(UUID notificationId) {
        notificationRepository.findById(notificationId)
                .ifPresent(notification -> {
                    notification.setIsPushed(true);
                    notification.setPushedAt(LocalDateTime.now());
                    notificationRepository.save(notification);
                });
    }

    /**
     * Get notifications that need to be pushed (for FCM retry).
     */
    public List<Notification> getUnpushedNotifications(LocalDateTime since) {
        return notificationRepository.findByIsPushedFalseAndCreatedAtAfterOrderByCreatedAtAsc(since);
    }

    /**
     * Delete old notifications (cleanup job).
     */
    @Transactional
    public int deleteOldNotifications(int daysToKeep) {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(daysToKeep);
        int deleted = notificationRepository.deleteOldNotifications(cutoff);
        log.info("Deleted {} old notifications (older than {} days)", deleted, daysToKeep);
        return deleted;
    }
}
