package com.lifepill.notification.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing a notification stored in the database.
 * Enables notification history, read status tracking, and offline delivery.
 */
@Entity
@Table(name = "notifications",
    indexes = {
        @Index(name = "idx_notification_user_id", columnList = "user_id"),
        @Index(name = "idx_notification_created_at", columnList = "created_at"),
        @Index(name = "idx_notification_read", columnList = "is_read"),
        @Index(name = "idx_notification_type", columnList = "type")
    }
)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "notification_id")
    private UUID id;
    
    @Column(name = "user_id", nullable = false)
    private UUID userId;
    
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationType type;
    
    @Column(name = "title", nullable = false)
    private String title;
    
    @Column(name = "message", length = 1000)
    private String message;
    
    @Column(name = "data", columnDefinition = "TEXT")
    private String data; // JSON data for the notification payload
    
    @Column(name = "reference_id")
    private UUID referenceId; // prescriptionId, orderId, etc.
    
    @Column(name = "reference_type")
    private String referenceType; // PRESCRIPTION, ORDER, etc.
    
    @Column(name = "is_read", nullable = false)
    @Builder.Default
    private Boolean isRead = false;
    
    @Column(name = "read_at")
    private LocalDateTime readAt;
    
    @Column(name = "is_pushed", nullable = false)
    @Builder.Default
    private Boolean isPushed = false; // For FCM push notifications
    
    @Column(name = "pushed_at")
    private LocalDateTime pushedAt;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    public enum NotificationType {
        PRESCRIPTION_UPLOADED,
        PRESCRIPTION_RESPONSE,
        ORDER_PLACED,
        ORDER_CONFIRMED,
        ORDER_READY,
        ORDER_DELIVERED,
        GENERAL
    }
}
