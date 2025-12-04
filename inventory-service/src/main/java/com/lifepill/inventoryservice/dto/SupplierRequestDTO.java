package com.lifepill.inventoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Supplier request DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierRequestDTO {
    private String supplierName;
    private String supplierAddress;
    private String supplierPhone;
    private String supplierEmail;
    private String supplierDescription;
    private String supplierRating;
    private Long companyId;
}
