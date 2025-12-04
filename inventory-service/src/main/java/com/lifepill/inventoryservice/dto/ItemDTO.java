package com.lifepill.inventoryservice.dto;

import com.lifepill.inventoryservice.entity.enums.MeasuringUnitType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Item DTO for API responses
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {
    private Long itemId;
    private String itemName;
    private Double sellingPrice;
    private String itemBarCode;
    private LocalDate supplyDate;
    private Double supplierPrice;
    private boolean isFreeIssued;
    private boolean isDiscounted;
    private String itemManufacture;
    private Double itemQuantity;
    private boolean stock;
    private MeasuringUnitType measuringUnitType;
    private LocalDate manufactureDate;
    private LocalDate expireDate;
    private LocalDate purchaseDate;
    private String warrantyPeriod;
    private String rackNumber;
    private Double discountedPrice;
    private Double discountedPercentage;
    private String warehouseName;
    private boolean isSpecialCondition;
    private String itemImage;
    private String itemDescription;
    private Long branchId;
    private Long categoryId;
    private String categoryName;
    private Long supplierId;
    private String supplierName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
