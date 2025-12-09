package com.lifepill.prescription.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "prescriptions", indexes = {
    @Index(name = "idx_user_id", columnList = "user_id"),
    @Index(name = "idx_status", columnList = "status"),
    @Index(name = "idx_upload_timestamp", columnList = "upload_timestamp")
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prescription {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "prescription_id")
    private UUID id;
    
    @Column(name = "user_id", nullable = false)
    private UUID userId;
    
    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    @Builder.Default
    private PrescriptionStatus status = PrescriptionStatus.UPLOADED;
    
    @Column(name = "upload_timestamp", nullable = false)
    private LocalDateTime uploadTimestamp;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PrescriptionResponse> responses = new ArrayList<>();
    
    public enum PrescriptionStatus {
        UPLOADED,
        PENDING_RESPONSES,
        RESPONDED,
        ORDER_PLACED,
        CANCELLED
    }
}
