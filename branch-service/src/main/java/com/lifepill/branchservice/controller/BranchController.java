package com.lifepill.branchservice.controller;

import com.lifepill.branchservice.dto.ApiResponse;
import com.lifepill.branchservice.dto.BranchDTO;
import com.lifepill.branchservice.dto.BranchRequestDTO;
import com.lifepill.branchservice.dto.BranchUpdateDTO;
import com.lifepill.branchservice.service.BranchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * REST Controller for Branch operations
 */
@RestController
@RequestMapping("/lifepill/v1/branch")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Branch Management", description = "APIs for managing pharmacy branches")
@CrossOrigin(origins = "*")
public class BranchController {
    
    private final BranchService branchService;
    
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
}
