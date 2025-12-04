package com.lifepill.possystem.service;

import com.lifepill.possystem.dto.requestDTO.BranchDailySalesSummaryDTO;
import com.lifepill.possystem.dto.responseDTO.BranchSalesDTO;
import com.lifepill.possystem.dto.responseDTO.DailySalesSummaryDTO;

import java.util.Date;
import java.util.List;

/**
 * Service interface for sales aggregation operations.
 * Provides methods to aggregate order/sales data by branch for other microservices.
 */
public interface SalesAggregationService {

    /**
     * Get sales summary grouped by branch
     */
    List<BranchSalesDTO> getSalesByBranch();

    /**
     * Get sales for a specific branch
     */
    BranchSalesDTO getSalesForBranch(Long branchId);

    /**
     * Get sales by date range grouped by branch
     */
    List<BranchSalesDTO> getSalesByPeriod(Date startDate, Date endDate);

    /**
     * Get sales by specific date grouped by branch
     */
    List<BranchSalesDTO> getSalesByDate(Date date);

    /**
     * Get monthly sales summary
     */
    List<BranchSalesDTO> getMonthlySales(int month, int year);

    /**
     * Get yearly sales summary
     */
    List<BranchSalesDTO> getYearlySales(int year);

    /**
     * Get total sales across all branches
     */
    Double getTotalSales();

    /**
     * Get total order count
     */
    Long getTotalOrderCount();

    /**
     * Get daily sales summary for a specific branch
     */
    List<DailySalesSummaryDTO> getDailySalesByBranch(Long branchId);

    /**
     * Get daily sales summary for all branches
     */
    List<BranchDailySalesSummaryDTO> getAllDailySales();
}
