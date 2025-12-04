package com.lifepill.branchservice.controller;

import com.lifepill.branchservice.dto.ApiResponse;
import com.lifepill.branchservice.dto.summary.AllPharmacySummaryResponseDTO;
import com.lifepill.branchservice.dto.summary.BranchDailySalesDTO;
import com.lifepill.branchservice.dto.summary.PharmacyBranchSummaryDTO;
import com.lifepill.branchservice.service.BranchSummaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * REST Controller for Branch Summary operations.
 * Provides aggregated data from multiple microservices.
 */
@RestController
@RequestMapping("/lifepill/v1/branch-summary")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "Branch Summary API", description = "Endpoints for branch summaries with aggregated data from Order, Inventory, and Identity services")
public class BranchSummaryController {

    private final BranchSummaryService branchSummaryService;

    @GetMapping("/all")
    @Operation(
        summary = "Get all pharmacy branch summaries",
        description = "Returns complete summary for all branches including sales, employees, and inventory data"
    )
    public ResponseEntity<ApiResponse<AllPharmacySummaryResponseDTO>> getAllPharmacySummary() {
        log.info("API call: Get all pharmacy summary");
        AllPharmacySummaryResponseDTO summary = branchSummaryService.getAllPharmacySummary();
        return ResponseEntity.ok(new ApiResponse<>(200, "All pharmacy summary retrieved successfully", summary));
    }

    @GetMapping("/{branchId}")
    @Operation(
        summary = "Get branch summary by ID",
        description = "Returns complete summary for a specific branch"
    )
    public ResponseEntity<ApiResponse<PharmacyBranchSummaryDTO>> getBranchSummary(
            @Parameter(description = "Branch ID") @PathVariable Long branchId) {
        log.info("API call: Get branch summary for ID: {}", branchId);
        PharmacyBranchSummaryDTO summary = branchSummaryService.getBranchSummary(branchId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Branch summary retrieved successfully", summary));
    }

    @GetMapping("/daily-sales")
    @Operation(
        summary = "Get daily sales summary for all branches",
        description = "Returns daily sales breakdown for all branches"
    )
    public ResponseEntity<ApiResponse<List<BranchDailySalesDTO>>> getDailySalesSummary() {
        log.info("API call: Get daily sales summary for all branches");
        List<BranchDailySalesDTO> dailySales = branchSummaryService.getDailySalesSummary();
        return ResponseEntity.ok(new ApiResponse<>(200, "Daily sales summary retrieved successfully", dailySales));
    }

    @GetMapping("/daily-sales/{branchId}")
    @Operation(
        summary = "Get daily sales summary for a specific branch",
        description = "Returns daily sales breakdown for a specific branch"
    )
    public ResponseEntity<ApiResponse<BranchDailySalesDTO>> getDailySalesSummaryForBranch(
            @Parameter(description = "Branch ID") @PathVariable Long branchId) {
        log.info("API call: Get daily sales summary for branch ID: {}", branchId);
        BranchDailySalesDTO dailySales = branchSummaryService.getDailySalesSummaryForBranch(branchId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Daily sales summary retrieved successfully", dailySales));
    }

    @GetMapping("/by-period")
    @Operation(
        summary = "Get sales by period",
        description = "Returns branch summaries with sales data for a specific date range"
    )
    public ResponseEntity<ApiResponse<List<PharmacyBranchSummaryDTO>>> getSalesByPeriod(
            @Parameter(description = "Start date (yyyy-MM-dd)") 
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @Parameter(description = "End date (yyyy-MM-dd)") 
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        log.info("API call: Get sales by period from {} to {}", startDate, endDate);
        List<PharmacyBranchSummaryDTO> summaries = branchSummaryService.getSalesByPeriod(startDate, endDate);
        return ResponseEntity.ok(new ApiResponse<>(200, "Sales by period retrieved successfully", summaries));
    }

    @GetMapping("/monthly")
    @Operation(
        summary = "Get monthly sales",
        description = "Returns branch summaries with monthly sales data"
    )
    public ResponseEntity<ApiResponse<List<PharmacyBranchSummaryDTO>>> getMonthlySales(
            @Parameter(description = "Month (1-12)") @RequestParam("month") int month,
            @Parameter(description = "Year") @RequestParam("year") int year) {
        log.info("API call: Get monthly sales for {}/{}", month, year);
        List<PharmacyBranchSummaryDTO> summaries = branchSummaryService.getMonthlySales(month, year);
        return ResponseEntity.ok(new ApiResponse<>(200, "Monthly sales retrieved successfully", summaries));
    }

    @GetMapping("/yearly")
    @Operation(
        summary = "Get yearly sales",
        description = "Returns branch summaries with yearly sales data"
    )
    public ResponseEntity<ApiResponse<List<PharmacyBranchSummaryDTO>>> getYearlySales(
            @Parameter(description = "Year") @RequestParam("year") int year) {
        log.info("API call: Get yearly sales for {}", year);
        List<PharmacyBranchSummaryDTO> summaries = branchSummaryService.getYearlySales(year);
        return ResponseEntity.ok(new ApiResponse<>(200, "Yearly sales retrieved successfully", summaries));
    }

    @GetMapping("/total-sales")
    @Operation(
        summary = "Get total sales",
        description = "Returns total sales across all branches"
    )
    public ResponseEntity<ApiResponse<Double>> getTotalSales() {
        log.info("API call: Get total sales");
        Double totalSales = branchSummaryService.getTotalSales();
        return ResponseEntity.ok(new ApiResponse<>(200, "Total sales retrieved successfully", totalSales));
    }

    @GetMapping("/total-orders")
    @Operation(
        summary = "Get total order count",
        description = "Returns total number of orders across all branches"
    )
    public ResponseEntity<ApiResponse<Long>> getTotalOrderCount() {
        log.info("API call: Get total order count");
        Long orderCount = branchSummaryService.getTotalOrderCount();
        return ResponseEntity.ok(new ApiResponse<>(200, "Total order count retrieved successfully", orderCount));
    }
}
