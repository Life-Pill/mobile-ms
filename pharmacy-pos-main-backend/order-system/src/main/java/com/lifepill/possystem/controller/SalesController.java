package com.lifepill.possystem.controller;

import com.lifepill.possystem.dto.requestDTO.BranchDailySalesSummaryDTO;
import com.lifepill.possystem.dto.responseDTO.BranchSalesDTO;
import com.lifepill.possystem.dto.responseDTO.DailySalesSummaryDTO;
import com.lifepill.possystem.service.SalesAggregationService;
import com.lifepill.possystem.util.StandardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * REST Controller for sales aggregation endpoints.
 * Provides sales data for inter-service communication (e.g., Branch Service).
 * These endpoints are called by other microservices and should be open for inter-service communication.
 */
@RestController
@RequestMapping("lifepill/v1/sales")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Sales Aggregation", description = "Inter-service sales data endpoints")
public class SalesController {

    private final SalesAggregationService salesAggregationService;

    /**
     * Get sales summary grouped by branch
     *
     * @return ResponseEntity containing StandardResponse with status 200 (OK) and list of BranchSalesDTO
     */
    @GetMapping("/by-branch")
    @Operation(
            summary = "Get sales by branch",
            description = "Returns sales summary grouped by branch. Used by Branch Service."
    )
    public ResponseEntity<StandardResponse> getSalesByBranch() {
        log.info("API call: Get sales by branch");
        List<BranchSalesDTO> salesByBranch = salesAggregationService.getSalesByBranch();
        return new ResponseEntity<>(
                new StandardResponse(
                        200,
                        "SUCCESS",
                        salesByBranch
                ),
                HttpStatus.OK
        );
    }

    /**
     * Get sales for a specific branch
     *
     * @param branchId The ID of the branch
     * @return ResponseEntity containing StandardResponse with status 200 (OK) and BranchSalesDTO
     */
    @GetMapping("/by-branch/{branchId}")
    @Operation(
            summary = "Get sales for a specific branch",
            description = "Returns sales summary for a specific branch. Used by Branch Service."
    )
    public ResponseEntity<StandardResponse> getSalesForBranch(
            @Parameter(description = "Branch ID") @PathVariable Long branchId) {
        log.info("API call: Get sales for branch: {}", branchId);
        BranchSalesDTO salesForBranch = salesAggregationService.getSalesForBranch(branchId);
        return new ResponseEntity<>(
                new StandardResponse(
                        200,
                        "SUCCESS",
                        salesForBranch
                ),
                HttpStatus.OK
        );
    }

    /**
     * Get total sales across all branches
     *
     * @return ResponseEntity containing StandardResponse with status 200 (OK) and total sales amount
     */
    @GetMapping("/total")
    @Operation(
            summary = "Get total sales",
            description = "Returns total sales amount across all branches. Used by Branch Service."
    )
    public ResponseEntity<StandardResponse> getTotalSales() {
        log.info("API call: Get total sales");
        Double totalSales = salesAggregationService.getTotalSales();
        return new ResponseEntity<>(
                new StandardResponse(
                        200,
                        "SUCCESS",
                        totalSales
                ),
                HttpStatus.OK
        );
    }

    /**
     * Get total order count
     *
     * @return ResponseEntity containing StandardResponse with status 200 (OK) and total order count
     */
    @GetMapping("/count")
    @Operation(
            summary = "Get total order count",
            description = "Returns total order count across all branches. Used by Branch Service."
    )
    public ResponseEntity<StandardResponse> getTotalOrderCount() {
        log.info("API call: Get total order count");
        Long totalOrderCount = salesAggregationService.getTotalOrderCount();
        return new ResponseEntity<>(
                new StandardResponse(
                        200,
                        "SUCCESS",
                        totalOrderCount
                ),
                HttpStatus.OK
        );
    }

    /**
     * Get daily sales summary for a specific branch
     *
     * @param branchId The ID of the branch
     * @return ResponseEntity containing StandardResponse with status 200 (OK) and list of DailySalesSummaryDTO
     */
    @GetMapping("/daily/{branchId}")
    @Operation(
            summary = "Get daily sales for a branch",
            description = "Returns daily sales breakdown for a specific branch. Used by Branch Service."
    )
    public ResponseEntity<StandardResponse> getDailySalesByBranch(
            @Parameter(description = "Branch ID") @PathVariable Long branchId) {
        log.info("API call: Get daily sales for branch: {}", branchId);
        List<DailySalesSummaryDTO> dailySales = salesAggregationService.getDailySalesByBranch(branchId);
        return new ResponseEntity<>(
                new StandardResponse(
                        200,
                        "SUCCESS",
                        dailySales
                ),
                HttpStatus.OK
        );
    }

    /**
     * Get daily sales summary for all branches
     *
     * @return ResponseEntity containing StandardResponse with status 200 (OK) and list of BranchDailySalesSummaryDTO
     */
    @GetMapping("/daily-all")
    @Operation(
            summary = "Get daily sales for all branches",
            description = "Returns daily sales breakdown for all branches. Used by Branch Service."
    )
    public ResponseEntity<StandardResponse> getAllDailySales() {
        log.info("API call: Get daily sales for all branches");
        List<BranchDailySalesSummaryDTO> allDailySales = salesAggregationService.getAllDailySales();
        return new ResponseEntity<>(
                new StandardResponse(
                        200,
                        "SUCCESS",
                        allDailySales
                ),
                HttpStatus.OK
        );
    }

    /**
     * Get sales by date range
     *
     * @param startDate Start date
     * @param endDate   End date
     * @return ResponseEntity containing StandardResponse with status 200 (OK) and list of BranchSalesDTO
     */
    @GetMapping("/by-period")
    @Operation(
            summary = "Get sales by date range",
            description = "Returns sales summary for a specific date range. Used by Branch Service."
    )
    public ResponseEntity<StandardResponse> getSalesByPeriod(
            @Parameter(description = "Start date (yyyy-MM-dd)") @RequestParam Date startDate,
            @Parameter(description = "End date (yyyy-MM-dd)") @RequestParam Date endDate) {
        log.info("API call: Get sales by period: {} to {}", startDate, endDate);
        List<BranchSalesDTO> salesByPeriod = salesAggregationService.getSalesByPeriod(startDate, endDate);
        return new ResponseEntity<>(
                new StandardResponse(
                        200,
                        "SUCCESS",
                        salesByPeriod
                ),
                HttpStatus.OK
        );
    }

    /**
     * Get sales by specific date
     *
     * @param date The date to get sales for
     * @return ResponseEntity containing StandardResponse with status 200 (OK) and list of BranchSalesDTO
     */
    @GetMapping("/by-date")
    @Operation(
            summary = "Get sales by specific date",
            description = "Returns sales summary for a specific date. Used by Branch Service."
    )
    public ResponseEntity<StandardResponse> getSalesByDate(
            @Parameter(description = "Date (yyyy-MM-dd)") @RequestParam Date date) {
        log.info("API call: Get sales by date: {}", date);
        List<BranchSalesDTO> salesByDate = salesAggregationService.getSalesByDate(date);
        return new ResponseEntity<>(
                new StandardResponse(
                        200,
                        "SUCCESS",
                        salesByDate
                ),
                HttpStatus.OK
        );
    }

    /**
     * Get monthly sales summary
     *
     * @param month The month (1-12)
     * @param year  The year
     * @return ResponseEntity containing StandardResponse with status 200 (OK) and list of BranchSalesDTO
     */
    @GetMapping("/monthly")
    @Operation(
            summary = "Get monthly sales",
            description = "Returns sales summary for a specific month. Used by Branch Service."
    )
    public ResponseEntity<StandardResponse> getMonthlySales(
            @Parameter(description = "Month (1-12)") @RequestParam int month,
            @Parameter(description = "Year") @RequestParam int year) {
        log.info("API call: Get monthly sales for {}/{}", month, year);
        List<BranchSalesDTO> monthlySales = salesAggregationService.getMonthlySales(month, year);
        return new ResponseEntity<>(
                new StandardResponse(
                        200,
                        "SUCCESS",
                        monthlySales
                ),
                HttpStatus.OK
        );
    }

    /**
     * Get yearly sales summary
     *
     * @param year The year
     * @return ResponseEntity containing StandardResponse with status 200 (OK) and list of BranchSalesDTO
     */
    @GetMapping("/yearly")
    @Operation(
            summary = "Get yearly sales",
            description = "Returns sales summary for a specific year. Used by Branch Service."
    )
    public ResponseEntity<StandardResponse> getYearlySales(
            @Parameter(description = "Year") @RequestParam int year) {
        log.info("API call: Get yearly sales for {}", year);
        List<BranchSalesDTO> yearlySales = salesAggregationService.getYearlySales(year);
        return new ResponseEntity<>(
                new StandardResponse(
                        200,
                        "SUCCESS",
                        yearlySales
                ),
                HttpStatus.OK
        );
    }
}
