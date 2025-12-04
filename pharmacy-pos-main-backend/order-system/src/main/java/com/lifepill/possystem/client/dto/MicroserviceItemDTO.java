package com.lifepill.possystem.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Item data from Inventory Service
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MicroserviceItemDTO {
    private Long itemId;
    private String itemName;
    private Double sellingPrice;
    private String itemBarCode;
    private Double itemQuantity;
    private boolean stock;
    private String categoryName;
    private String supplierName;
    private Long branchId;
}
