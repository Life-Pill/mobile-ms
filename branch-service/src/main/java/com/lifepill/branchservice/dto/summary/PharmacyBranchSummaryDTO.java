package com.lifepill.branchservice.dto.summary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for pharmacy branch summary including sales, employees, and items count
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PharmacyBranchSummaryDTO {
    
    private Long branchId;
    private String branchName;
    private String branchAddress;
    private String branchContact;
    private String branchEmail;
    private String branchLocation;
    private boolean branchStatus;
    private String branchImageUrl;
    
    // Sales data (from Order Service)
    private Double totalSales;
    private Long orderCount;
    
    // Employee data (from Identity Service)
    private Long employeeCount;
    
    // Inventory data (from Inventory Service)
    private Long itemCount;
    private Long lowStockItemCount;
}
