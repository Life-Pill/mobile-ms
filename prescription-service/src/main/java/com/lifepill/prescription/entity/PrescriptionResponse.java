package com.lifepill.prescription.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "prescription_responses", 
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_prescription_branch", 
            columnNames = {"prescription_id", "branch_id"})
    },
    indexes = {
        @Index(name = "idx_prescription_id", columnList = "prescription_id"),
        @Index(name = "idx_branch_id", columnList = "branch_id")
    }
)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionResponse {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "response_id")
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescription_id", nullable = false)
    private Prescription prescription;
    
    @Column(name = "branch_id", nullable = false)
    private Long branchId;
    
    @Column(name = "employer_id", nullable = false)
    private UUID employerId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    @Builder.Default
    private ResponseStatus status = ResponseStatus.REVIEWING;
    
    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "response_timestamp", nullable = false)
    private LocalDateTime responseTimestamp;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "response", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<MedicineAvailability> medicineAvailabilities = new ArrayList<>();
    
    public enum ResponseStatus {
        REVIEWING,
        AVAILABLE,
        PARTIALLY_AVAILABLE,
        NOT_AVAILABLE
    }
}
