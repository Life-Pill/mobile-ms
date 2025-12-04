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
 * REST Controller for Branch Summary operations (Legacy/Deprecated).
 * 
 * @deprecated Use {@link BranchController} endpoints under /lifepill/v1/branch/summary/* instead.
 *             These endpoints are maintained for backward compatibility.
 * 
 * New Endpoints:
 * - /lifepill/v1/branch/summary/all
 * - /lifepill/v1/branch/summary/{branchId}
 * - /lifepill/v1/branch/summary/daily-sales
 * - /lifepill/v1/branch/summary/daily-sales/{branchId}
 * - /lifepill/v1/branch/summary/by-period
 * - /lifepill/v1/branch/summary/monthly
 * - /lifepill/v1/branch/summary/yearly
 * - /lifepill/v1/branch/summary/total-sales
 * - /lifepill/v1/branch/summary/total-orders
 */
@Deprecated(since = "2.0.0", forRemoval = true)
@RestController
@RequestMapping("/lifepill/v1/branch-summary")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "Branch Summary API (Deprecated)", description = "DEPRECATED - Use /lifepill/v1/branch/summary/* endpoints instead")
public class BranchSummaryController {

    private final BranchSummaryService branchSummaryService;

    @Deprecated
    @GetMapping("/all")
    @Operation(
        summary = "Get all pharmacy branch summaries (DEPRECATED)",
        description = "DEPRECATED - Use /lifepill/v1/branch/summary/all instead"
    )
    public ResponseEntity<ApiResponse<AllPharmacySummaryResponseDTO>> getAllPharmacySummary() {
        log.warn("DEPRECATED API call: /lifepill/v1/branch-summary/all - Use /lifepill/v1/branch/summary/all instead");
        AllPharmacySummaryResponseDTO summary = branchSummaryService.getAllPharmacySummary();
        return ResponseEntity.ok(new ApiResponse<>(200, "All pharmacy summary retrieved successfully (DEPRECATED - Use /lifepill/v1/branch/summary/all)", summary));
    }

    @Deprecated
    @GetMapping("/{branchId}")
    @Operation(
        summary = "Get branch summary by ID (DEPRECATED)",
        description = "DEPRECATED - Use /lifepill/v1/branch/summary/{branchId} instead"
    )
    public ResponseEntity<ApiResponse<PharmacyBranchSummaryDTO>> getBranchSummary(
            @Parameter(description = "Branch ID") @PathVariable Long branchId) {
        log.warn("DEPRECATED API call: /lifepill/v1/branch-summary/{} - Use /lifepill/v1/branch/summary/{} instead", branchId, branchId);
        PharmacyBranchSummaryDTO summary = branchSummaryService.getBranchSummary(branchId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Branch summary retrieved successfully (DEPRECATED)", summary));
    }

    @Deprecated
    @GetMapping("/daily-sales")
    @Operation(
        summary = "Get daily sales summary for all branches (DEPRECATED)",
        description = "DEPRECATED - Use /lifepill/v1/branch/summary/daily-sales instead"
    )
    public ResponseEntity<ApiResponse<List<BranchDailySalesDTO>>> getDailySalesSummary() {
        log.warn("DEPRECATED API call: /lifepill/v1/branch-summary/daily-sales - Use /lifepill/v1/branch/summary/daily-sales instead");
        List<BranchDailySalesDTO> dailySales = branchSummaryService.getDailySalesSummary();
        return ResponseEntity.ok(new ApiResponse<>(200, "Daily sales summary retrieved successfully (DEPRECATED)", dailySales));
    }

    @Deprecated
    @GetMapping("/daily-sales/{branchId}")
    @Operation(
        summary = "Get daily sales summary for a specific branch (DEPRECATED)",
        description = "DEPRECATED - Use /lifepill/v1/branch/summary/daily-sales/{branchId} instead"
    )
    public ResponseEntity<ApiResponse<BranchDailySalesDTO>> getDailySalesSummaryForBranch(
            @Parameter(description = "Branch ID") @PathVariable Long branchId) {
        log.warn("DEPRECATED API call: /lifepill/v1/branch-summary/daily-sales/{} - Use /lifepill/v1/branch/summary/daily-sales/{} instead", branchId, branchId);
        BranchDailySalesDTO dailySales = branchSummaryService.getDailySalesSummaryForBranch(branchId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Daily sales summary retrieved successfully (DEPRECATED)", dailySales));
    }

    @Deprecated
    @GetMapping("/by-period")
    @Operation(
        summary = "Get sales by period (DEPRECATED)",
        description = "DEPRECATED - Use /lifepill/v1/branch/summary/by-period instead"
    )
    public ResponseEntity<ApiResponse<List<PharmacyBranchSummaryDTO>>> getSalesByPeriod(
            @Parameter(description = "Start date (yyyy-MM-dd)") 
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @Parameter(description = "End date (yyyy-MM-dd)") 
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        log.warn("DEPRECATED API call: /lifepill/v1/branch-summary/by-period - Use /lifepill/v1/branch/summary/by-period instead");
        List<PharmacyBranchSummaryDTO> summaries = branchSummaryService.getSalesByPeriod(startDate, endDate);
        return ResponseEntity.ok(new ApiResponse<>(200, "Sales by period retrieved successfully (DEPRECATED)", summaries));
    }

    @Deprecated
    @GetMapping("/monthly")
    @Operation(
        summary = "Get monthly sales (DEPRECATED)",
        description = "DEPRECATED - Use /lifepill/v1/branch/summary/monthly instead"
    )
    public ResponseEntity<ApiResponse<List<PharmacyBranchSummaryDTO>>> getMonthlySales(
            @Parameter(description = "Month (1-12)") @RequestParam("month") int month,
            @Parameter(description = "Year") @RequestParam("year") int year) {
        log.warn("DEPRECATED API call: /lifepill/v1/branch-summary/monthly - Use /lifepill/v1/branch/summary/monthly instead");
        List<PharmacyBranchSummaryDTO> summaries = branchSummaryService.getMonthlySales(month, year);
        return ResponseEntity.ok(new ApiResponse<>(200, "Monthly sales retrieved successfully (DEPRECATED)", summaries));
    }

    @Deprecated
    @GetMapping("/yearly")
    @Operation(
        summary = "Get yearly sales (DEPRECATED)",
        description = "DEPRECATED - Use /lifepill/v1/branch/summary/yearly instead"
    )
    public ResponseEntity<ApiResponse<List<PharmacyBranchSummaryDTO>>> getYearlySales(
            @Parameter(description = "Year") @RequestParam("year") int year) {
        log.warn("DEPRECATED API call: /lifepill/v1/branch-summary/yearly - Use /lifepill/v1/branch/summary/yearly instead");
        List<PharmacyBranchSummaryDTO> summaries = branchSummaryService.getYearlySales(year);
        return ResponseEntity.ok(new ApiResponse<>(200, "Yearly sales retrieved successfully (DEPRECATED)", summaries));
    }

    @Deprecated
    @GetMapping("/total-sales")
    @Operation(
        summary = "Get total sales (DEPRECATED)",
        description = "DEPRECATED - Use /lifepill/v1/branch/summary/total-sales instead"
    )
    public ResponseEntity<ApiResponse<Double>> getTotalSales() {
        log.warn("DEPRECATED API call: /lifepill/v1/branch-summary/total-sales - Use /lifepill/v1/branch/summary/total-sales instead");
        Double totalSales = branchSummaryService.getTotalSales();
        return ResponseEntity.ok(new ApiResponse<>(200, "Total sales retrieved successfully (DEPRECATED)", totalSales));
    }

    @Deprecated
    @GetMapping("/total-orders")
    @Operation(
        summary = "Get total order count (DEPRECATED)",
        description = "DEPRECATED - Use /lifepill/v1/branch/summary/total-orders instead"
    )
    public ResponseEntity<ApiResponse<Long>> getTotalOrderCount() {
        log.warn("DEPRECATED API call: /lifepill/v1/branch-summary/total-orders - Use /lifepill/v1/branch/summary/total-orders instead");
        Long orderCount = branchSummaryService.getTotalOrderCount();
        return ResponseEntity.ok(new ApiResponse<>(200, "Total order count retrieved successfully (DEPRECATED)", orderCount));
    }
}
