package com.lifepill.inventoryservice.entity;

import com.lifepill.inventoryservice.entity.enums.MeasuringUnitType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Item entity representing inventory items/medicines
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "item")
public class Item {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long itemId;
    
    @Column(name = "item_name", length = 100, nullable = false)
    private String itemName;
    
    @Column(name = "selling_price", nullable = false)
    private Double sellingPrice;
    
    @Column(name = "item_barcode", length = 50, nullable = false)
    private String itemBarCode;
    
    @Column(name = "supply_date")
    private LocalDate supplyDate;
    
    @Column(name = "supplier_price", nullable = false)
    private Double supplierPrice;
    
    @Column(name = "is_free_issued", columnDefinition = "BOOLEAN default false")
    private boolean isFreeIssued;
    
    @Column(name = "is_discounted", columnDefinition = "BOOLEAN default false")
    private boolean isDiscounted;
    
    @Column(name = "item_manufacture", length = 100)
    private String itemManufacture;
    
    @Column(name = "item_quantity", nullable = false)
    private Double itemQuantity;
    
    @Column(name = "is_stock", columnDefinition = "BOOLEAN default true")
    private boolean stock;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "measuring_unit_type", length = 30)
    private MeasuringUnitType measuringUnitType;
    
    @Column(name = "manufacture_date")
    private LocalDate manufactureDate;
    
    @Column(name = "expire_date")
    private LocalDate expireDate;
    
    @Column(name = "purchase_date")
    private LocalDate purchaseDate;
    
    @Column(name = "warranty_period", length = 50)
    private String warrantyPeriod;
    
    @Column(name = "rack_number", length = 50)
    private String rackNumber;
    
    @Column(name = "discounted_price")
    private Double discountedPrice;
    
    @Column(name = "discounted_percentage")
    private Double discountedPercentage;
    
    @Column(name = "warehouse_name", length = 100)
    private String warehouseName;
    
    @Column(name = "is_special_condition", columnDefinition = "BOOLEAN default false")
    private boolean isSpecialCondition;
    
    @Column(name = "item_image", length = 500)
    private String itemImage;
    
    @Column(name = "item_description", length = 500)
    private String itemDescription;
    
    @Column(name = "branch_id")
    private Long branchId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private ItemCategory itemCategory;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
