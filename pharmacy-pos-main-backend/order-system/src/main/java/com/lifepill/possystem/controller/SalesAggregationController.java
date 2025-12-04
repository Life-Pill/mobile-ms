package com.lifepill.possystem.controller;

import com.lifepill.possystem.dto.responseDTO.BranchSalesDTO;
import com.lifepill.possystem.dto.responseDTO.DailySalesSummaryDTO;
import com.lifepill.possystem.service.SalesAggregationService;
import com.lifepill.possystem.util.StandardResponse;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Controller for exposing sales aggregation data to other microservices.
 * This controller provides endpoints that branch-service can call via OpenFeign
 * to get order/sales data for branch summaries.
 */
@RestController
@RequestMapping("/lifepill/v1/sales")
@AllArgsConstructor
public class SalesAggregationController {

    private final SalesAggregationService salesAggregationService;

    /**
     * Get sales summary for all branches
     */
    @GetMapping("/by-branch")
    public ResponseEntity<StandardResponse> getSalesByBranch() {
        List<BranchSalesDTO> salesData = salesAggregationService.getSalesByBranch();
        return new ResponseEntity<>(
                new StandardResponse(200, "Sales data retrieved successfully", salesData),
                HttpStatus.OK
        );
    }

    /**
     * Get sales for a specific branch
     */
    @GetMapping("/by-branch/{branchId}")
    public ResponseEntity<StandardResponse> getSalesForBranch(@PathVariable Long branchId) {
        BranchSalesDTO salesData = salesAggregationService.getSalesForBranch(branchId);
        return new ResponseEntity<>(
                new StandardResponse(200, "Sales data for branch retrieved successfully", salesData),
                HttpStatus.OK
        );
    }

    /**
     * Get sales by date range grouped by branch
     */
    @GetMapping("/by-period")
    public ResponseEntity<StandardResponse> getSalesByPeriod(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        List<BranchSalesDTO> salesData = salesAggregationService.getSalesByPeriod(startDate, endDate);
        return new ResponseEntity<>(
                new StandardResponse(200, "Sales data for period retrieved successfully", salesData),
                HttpStatus.OK
        );
    }

    /**
     * Get sales by date for all branches
     */
    @GetMapping("/by-date")
    public ResponseEntity<StandardResponse> getSalesByDate(
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        List<BranchSalesDTO> salesData = salesAggregationService.getSalesByDate(date);
        return new ResponseEntity<>(
                new StandardResponse(200, "Sales data for date retrieved successfully", salesData),
                HttpStatus.OK
        );
    }

    /**
     * Get monthly sales summary
     */
    @GetMapping("/monthly")
    public ResponseEntity<StandardResponse> getMonthlySales(
            @RequestParam("month") int month,
            @RequestParam("year") int year) {
        List<BranchSalesDTO> salesData = salesAggregationService.getMonthlySales(month, year);
        return new ResponseEntity<>(
                new StandardResponse(200, "Monthly sales data retrieved successfully", salesData),
                HttpStatus.OK
        );
    }

    /**
     * Get yearly sales summary
     */
    @GetMapping("/yearly")
    public ResponseEntity<StandardResponse> getYearlySales(@RequestParam("year") int year) {
        List<BranchSalesDTO> salesData = salesAggregationService.getYearlySales(year);
        return new ResponseEntity<>(
                new StandardResponse(200, "Yearly sales data retrieved successfully", salesData),
                HttpStatus.OK
        );
    }

    /**
     * Get total sales across all branches
     */
    @GetMapping("/total")
    public ResponseEntity<StandardResponse> getTotalSales() {
        Double totalSales = salesAggregationService.getTotalSales();
        return new ResponseEntity<>(
                new StandardResponse(200, "Total sales retrieved successfully", totalSales),
                HttpStatus.OK
        );
    }

    /**
     * Get total order count
     */
    @GetMapping("/count")
    public ResponseEntity<StandardResponse> getTotalOrderCount() {
        Long count = salesAggregationService.getTotalOrderCount();
        return new ResponseEntity<>(
                new StandardResponse(200, "Order count retrieved successfully", count),
                HttpStatus.OK
        );
    }

    /**
     * Get daily sales summary for a specific branch
     */
    @GetMapping("/daily/{branchId}")
    public ResponseEntity<StandardResponse> getDailySalesByBranch(@PathVariable Long branchId) {
        List<DailySalesSummaryDTO> dailySales = salesAggregationService.getDailySalesByBranch(branchId);
        return new ResponseEntity<>(
                new StandardResponse(200, "Daily sales data retrieved successfully", dailySales),
                HttpStatus.OK
        );
    }

    /**
     * Get daily sales summary for all branches
     */
    @GetMapping("/daily-all")
    public ResponseEntity<StandardResponse> getAllDailySales() {
        var allDailySales = salesAggregationService.getAllDailySales();
        return new ResponseEntity<>(
                new StandardResponse(200, "All daily sales data retrieved successfully", allDailySales),
                HttpStatus.OK
        );
    }
}
