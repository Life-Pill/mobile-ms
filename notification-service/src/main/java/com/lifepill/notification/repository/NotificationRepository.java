package com.lifepill.notification.repository;

import com.lifepill.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repository for notification persistence operations.
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    
    /**
     * Find all notifications for a user, ordered by creation date (newest first).
     */
    Page<Notification> findByUserIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);
    
    /**
     * Find unread notifications for a user.
     */
    List<Notification> findByUserIdAndIsReadFalseOrderByCreatedAtDesc(UUID userId);
    
    /**
     * Count unread notifications for a user.
     */
    long countByUserIdAndIsReadFalse(UUID userId);
    
    /**
     * Count total notifications for a user.
     */
    long countByUserId(UUID userId);
    
    /**
     * Find notifications by type for a user.
     */
    List<Notification> findByUserIdAndTypeOrderByCreatedAtDesc(UUID userId, Notification.NotificationType type);
    
    /**
     * Find notifications that haven't been pushed (for FCM retry).
     */
    List<Notification> findByIsPushedFalseAndCreatedAtAfterOrderByCreatedAtAsc(LocalDateTime after);
    
    /**
     * Mark all notifications as read for a user.
     */
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true, n.readAt = :readAt WHERE n.userId = :userId AND n.isRead = false")
    int markAllAsRead(@Param("userId") UUID userId, @Param("readAt") LocalDateTime readAt);
    
    /**
     * Find notifications by reference (e.g., all notifications for a prescription).
     */
    List<Notification> findByReferenceIdAndReferenceTypeOrderByCreatedAtDesc(UUID referenceId, String referenceType);
    
    /**
     * Delete old notifications (for cleanup).
     */
    @Modifying
    @Query("DELETE FROM Notification n WHERE n.createdAt < :cutoffDate")
    int deleteOldNotifications(@Param("cutoffDate") LocalDateTime cutoffDate);
}
