package com.lifepill.branchservice.service;

import com.lifepill.branchservice.dto.summary.*;

import java.util.Date;
import java.util.List;

/**
 * Service interface for Branch Summary operations.
 * Aggregates data from Branch, Order (via Feign), Inventory (via Feign), and Identity (via Feign) services.
 */
public interface BranchSummaryService {
    
    /**
     * Get complete summary for all pharmacy branches
     * Including sales, employee count, and item count
     */
    AllPharmacySummaryResponseDTO getAllPharmacySummary();
    
    /**
     * Get complete summary for a specific branch
     */
    PharmacyBranchSummaryDTO getBranchSummary(Long branchId);
    
    /**
     * Get daily sales summary for all branches
     */
    List<BranchDailySalesDTO> getDailySalesSummary();
    
    /**
     * Get daily sales summary for a specific branch
     */
    BranchDailySalesDTO getDailySalesSummaryForBranch(Long branchId);
    
    /**
     * Get sales summary for a specific period
     */
    List<PharmacyBranchSummaryDTO> getSalesByPeriod(Date startDate, Date endDate);
    
    /**
     * Get monthly sales summary
     */
    List<PharmacyBranchSummaryDTO> getMonthlySales(int month, int year);
    
    /**
     * Get yearly sales summary
     */
    List<PharmacyBranchSummaryDTO> getYearlySales(int year);
    
    /**
     * Get total sales across all branches
     */
    Double getTotalSales();
    
    /**
     * Get total order count across all branches
     */
    Long getTotalOrderCount();
}
