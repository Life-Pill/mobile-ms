package com.lifepill.branchservice.controller;

import com.lifepill.branchservice.dto.ApiResponse;
import com.lifepill.branchservice.dto.BranchDTO;
import com.lifepill.branchservice.dto.BranchRequestDTO;
import com.lifepill.branchservice.dto.BranchUpdateDTO;
import com.lifepill.branchservice.dto.summary.AllPharmacySummaryResponseDTO;
import com.lifepill.branchservice.dto.summary.BranchDailySalesDTO;
import com.lifepill.branchservice.dto.summary.PharmacyBranchSummaryDTO;
import com.lifepill.branchservice.service.BranchService;
import com.lifepill.branchservice.service.BranchSummaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * REST Controller for Branch operations and Branch Summary operations.
 * Provides CRUD operations for branches and aggregated data from multiple microservices.
 */
@RestController
@RequestMapping("/lifepill/v1/branch")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Branch Management", description = "APIs for managing pharmacy branches including CRUD operations and sales summaries")
@CrossOrigin(origins = "*")
public class BranchController {
    
    private final BranchService branchService;
    private final BranchSummaryService branchSummaryService;
    
    @PostMapping("/save")
    @Operation(summary = "Create a new branch", description = "Create a new pharmacy branch")
    public ResponseEntity<ApiResponse<BranchDTO>> createBranch(@RequestBody BranchRequestDTO requestDTO) {
        log.info("Creating new branch: {}", requestDTO.getBranchName());
        BranchDTO createdBranch = branchService.createBranch(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(createdBranch));
    }
    
    @GetMapping("/get-by-id")
    @Operation(summary = "Get branch by ID", description = "Retrieve a branch by its ID")
    public ResponseEntity<ApiResponse<BranchDTO>> getBranchById(@RequestParam Long branchId) {
        log.info("Fetching branch with ID: {}", branchId);
        BranchDTO branch = branchService.getBranchById(branchId);
        return ResponseEntity.ok(ApiResponse.success(branch));
    }
    
    @GetMapping("/get-all-branches")
    @Operation(summary = "Get all branches", description = "Retrieve all pharmacy branches")
    public ResponseEntity<ApiResponse<List<BranchDTO>>> getAllBranches() {
        log.info("Fetching all branches");
        List<BranchDTO> branches = branchService.getAllBranches();
        return ResponseEntity.ok(ApiResponse.success(branches));
    }
    
    @GetMapping("/get-active-branches")
    @Operation(summary = "Get active branches", description = "Retrieve all active pharmacy branches")
    public ResponseEntity<ApiResponse<List<BranchDTO>>> getActiveBranches() {
        log.info("Fetching active branches");
        List<BranchDTO> branches = branchService.getActiveBranches();
        return ResponseEntity.ok(ApiResponse.success(branches));
    }
    
    @PutMapping("/update")
    @Operation(summary = "Update branch", description = "Update an existing branch")
    public ResponseEntity<ApiResponse<BranchDTO>> updateBranch(
            @RequestParam Long branchId,
            @RequestBody BranchUpdateDTO updateDTO) {
        log.info("Updating branch with ID: {}", branchId);
        BranchDTO updatedBranch = branchService.updateBranch(branchId, updateDTO);
        return ResponseEntity.ok(ApiResponse.success("Branch updated successfully", updatedBranch));
    }
    
    @DeleteMapping("/delete-by-id")
    @Operation(summary = "Delete branch", description = "Delete a branch by its ID")
    public ResponseEntity<ApiResponse<String>> deleteBranch(@RequestParam Long branchId) {
        log.info("Deleting branch with ID: {}", branchId);
        branchService.deleteBranch(branchId);
        return ResponseEntity.ok(ApiResponse.success("Branch deleted successfully", null));
    }
    
    @PutMapping("/update-status")
    @Operation(summary = "Update branch status", description = "Update the active status of a branch")
    public ResponseEntity<ApiResponse<BranchDTO>> updateBranchStatus(
            @RequestParam Long branchId,
            @RequestParam boolean status) {
        log.info("Updating branch status for ID: {} to {}", branchId, status);
        BranchDTO updatedBranch = branchService.updateBranchStatus(branchId, status);
        return ResponseEntity.ok(ApiResponse.success("Branch status updated successfully", updatedBranch));
    }
    
    @PostMapping(value = "/update-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update branch image", description = "Upload and update branch image")
    public ResponseEntity<ApiResponse<BranchDTO>> updateBranchImage(
            @RequestParam Long branchId,
            @RequestParam("image") MultipartFile image) {
        log.info("Updating image for branch ID: {}", branchId);
        BranchDTO updatedBranch = branchService.updateBranchImage(branchId, image);
        return ResponseEntity.ok(ApiResponse.success("Branch image updated successfully", updatedBranch));
    }
    
    @GetMapping("/search-by-name")
    @Operation(summary = "Search branches by name", description = "Search branches by branch name")
    public ResponseEntity<ApiResponse<List<BranchDTO>>> searchBranchesByName(@RequestParam String name) {
        log.info("Searching branches by name: {}", name);
        List<BranchDTO> branches = branchService.searchBranchesByName(name);
        return ResponseEntity.ok(ApiResponse.success(branches));
    }
    
    @GetMapping("/search-by-location")
    @Operation(summary = "Search branches by location", description = "Search branches by location")
    public ResponseEntity<ApiResponse<List<BranchDTO>>> searchBranchesByLocation(@RequestParam String location) {
        log.info("Searching branches by location: {}", location);
        List<BranchDTO> branches = branchService.searchBranchesByLocation(location);
        return ResponseEntity.ok(ApiResponse.success(branches));
    }
    
    @GetMapping("/get-with-employers")
    @Operation(summary = "Get branch with employers", description = "Retrieve a branch with its employers")
    public ResponseEntity<ApiResponse<BranchDTO>> getBranchWithEmployers(@RequestParam Long branchId) {
        log.info("Fetching branch with employers for ID: {}", branchId);
        BranchDTO branch = branchService.getBranchWithEmployers(branchId);
        return ResponseEntity.ok(ApiResponse.success(branch));
    }
    
    @GetMapping("/exists")
    @Operation(summary = "Check if branch exists", description = "Check if a branch exists by ID")
    public ResponseEntity<ApiResponse<Boolean>> branchExists(@RequestParam Long branchId) {
        log.info("Checking if branch exists with ID: {}", branchId);
        boolean exists = branchService.branchExists(branchId);
        return ResponseEntity.ok(ApiResponse.success(exists));
    }
    
    @GetMapping("/count")
    @Operation(summary = "Count all branches", description = "Get total count of branches")
    public ResponseEntity<ApiResponse<Long>> countBranches() {
        log.info("Counting all branches");
        long count = branchService.countBranches();
        return ResponseEntity.ok(ApiResponse.success(count));
    }
    
    // ============================================
    // BRANCH SUMMARY OPERATIONS (Sales & Analytics)
    // ============================================
    
    @GetMapping("/summary/all")
    @Operation(
        summary = "Get all pharmacy branch summaries",
        description = "Returns complete summary for all branches including sales, employees, and inventory data"
    )
    public ResponseEntity<ApiResponse<AllPharmacySummaryResponseDTO>> getAllPharmacySummary() {
        log.info("API call: Get all pharmacy summary");
        AllPharmacySummaryResponseDTO summary = branchSummaryService.getAllPharmacySummary();
        return ResponseEntity.ok(new ApiResponse<>(200, "All pharmacy summary retrieved successfully", summary));
    }

    @GetMapping("/summary/{branchId}")
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

    @GetMapping("/summary/daily-sales")
    @Operation(
        summary = "Get daily sales summary for all branches",
        description = "Returns daily sales breakdown for all branches"
    )
    public ResponseEntity<ApiResponse<List<BranchDailySalesDTO>>> getDailySalesSummary() {
        log.info("API call: Get daily sales summary for all branches");
        List<BranchDailySalesDTO> dailySales = branchSummaryService.getDailySalesSummary();
        return ResponseEntity.ok(new ApiResponse<>(200, "Daily sales summary retrieved successfully", dailySales));
    }

    @GetMapping("/summary/daily-sales/{branchId}")
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

    @GetMapping("/summary/by-period")
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

    @GetMapping("/summary/monthly")
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

    @GetMapping("/summary/yearly")
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

    @GetMapping("/summary/total-sales")
    @Operation(
        summary = "Get total sales",
        description = "Returns total sales across all branches"
    )
    public ResponseEntity<ApiResponse<Double>> getTotalSales() {
        log.info("API call: Get total sales");
        Double totalSales = branchSummaryService.getTotalSales();
        return ResponseEntity.ok(new ApiResponse<>(200, "Total sales retrieved successfully", totalSales));
    }

    @GetMapping("/summary/total-orders")
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
