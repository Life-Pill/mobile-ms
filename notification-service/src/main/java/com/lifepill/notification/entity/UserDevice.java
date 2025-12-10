package com.lifepill.notification.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_devices",
    indexes = {
        @Index(name = "idx_user_device_user_id", columnList = "user_id"),
        @Index(name = "idx_user_device_fcm_token", columnList = "fcm_token")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_device_id", columnNames = "device_id")
    }
)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDevice {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;
    
    @Column(name = "user_id", nullable = false)
    private UUID userId;
    
    @Column(name = "fcm_token", nullable = false, length = 500)
    private String fcmToken;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "device_type", nullable = false, length = 20)
    private DeviceType deviceType;
    
    @Column(name = "device_id", nullable = false, length = 200)
    private String deviceId;
    
    @Column(name = "device_name", length = 100)
    private String deviceName;
    
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    public enum DeviceType {
        ANDROID,
        IOS,
        WEB
    }
}
