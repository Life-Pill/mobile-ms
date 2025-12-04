package com.lifepill.inventoryservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Supplier entity
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "supplier")
public class Supplier {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplier_id")
    private Long supplierId;
    
    @Column(name = "supplier_name", length = 100)
    private String supplierName;
    
    @Column(name = "supplier_address", length = 255)
    private String supplierAddress;
    
    @Column(name = "supplier_phone", length = 20)
    private String supplierPhone;
    
    @Column(name = "supplier_email", length = 100)
    private String supplierEmail;
    
    @Column(name = "supplier_description", length = 500)
    private String supplierDescription;
    
    @Column(name = "supplier_image", length = 500)
    private String supplierImage;
    
    @Column(name = "supplier_rating", length = 20)
    private String supplierRating;
    
    @Column(name = "is_active", columnDefinition = "BOOLEAN default true")
    private boolean isActive;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private SupplierCompany supplierCompany;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "supplier", fetch = FetchType.LAZY)
    private Set<Item> items = new HashSet<>();
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
