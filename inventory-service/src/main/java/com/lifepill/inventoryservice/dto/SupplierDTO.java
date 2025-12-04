package com.lifepill.inventoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Supplier DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierDTO {
    private Long supplierId;
    private String supplierName;
    private String supplierAddress;
    private String supplierPhone;
    private String supplierEmail;
    private String supplierDescription;
    private String supplierImage;
    private String supplierRating;
    private boolean isActive;
    private Long companyId;
    private String companyName;
}
