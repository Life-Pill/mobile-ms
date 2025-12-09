package com.lifepill.prescription.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "medicine_availability",
    indexes = {
        @Index(name = "idx_response_id", columnList = "response_id")
    }
)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicineAvailability {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "availability_id")
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "response_id", nullable = false)
    private PrescriptionResponse response;
    
    @Column(name = "medicine_name", nullable = false)
    private String medicineName;
    
    @Column(name = "item_id")
    private Long itemId; // Link to inventory service item
    
    @Column(name = "item_bar_code")
    private String itemBarCode;
    
    @Column(name = "measuring_unit_type")
    private String measuringUnitType; // TABLETS, CAPSULES, ML, etc.
    
    @Column(name = "branch_id")
    private Long branchId; // Which branch has this medicine
    
    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable;
    
    @Column(name = "stock")
    private Boolean stock; // Whether item is in stock at branch
    
    @Column(name = "quantity_available")
    private Integer quantityAvailable;
    
    @Column(name = "unit_price", precision = 10, scale = 2)
    private BigDecimal unitPrice;
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
