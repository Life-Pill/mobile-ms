package com.lifepill.branchservice.controller;

import com.lifepill.branchservice.dto.ApiResponse;
import com.lifepill.branchservice.dto.manager.*;
import com.lifepill.branchservice.service.BranchManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Branch Manager operations.
 * Provides endpoints for managing branch managers and employers.
 */
@RestController
@RequestMapping("/lifepill/v1/branch-manager")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Branch Manager", description = "APIs for managing branch managers and employers")
@CrossOrigin(origins = "*")
public class BranchManagerController {

    private final BranchManagerService branchManagerService;

    /**
     * Get all managers for a branch.
     */
    @GetMapping("/managers/by-branch/{branchId}")
    @Operation(summary = "Get managers by branch", description = "Get all managers for a specific branch")
    public ResponseEntity<ApiResponse<List<BranchManagerDTO>>> getManagersByBranch(
            @Parameter(description = "Branch ID") @PathVariable Long branchId) {
        log.info("Getting managers for branch ID: {}", branchId);
        List<BranchManagerDTO> managers = branchManagerService.getManagersByBranch(branchId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", managers));
    }

    /**
     * Update or create a branch manager.
     */
    @PutMapping("/managers/by-branch/{branchId}")
    @Operation(summary = "Update or create manager", description = "Update or create a manager for a specific branch")
    public ResponseEntity<ApiResponse<BranchManagerDTO>> updateOrCreateManager(
            @Parameter(description = "Branch ID") @PathVariable Long branchId,
            @RequestBody UpdateManagerRequestDTO requestDTO) {
        log.info("Update or create manager for branch ID: {}", branchId);
        BranchManagerDTO manager = branchManagerService.updateOrCreateManager(branchId, requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(200, "Manager created/updated successfully", manager));
    }

    /**
     * Change the branch manager.
     */
    @PostMapping("/change-manager")
    @Operation(summary = "Change branch manager", description = "Change the manager of a branch")
    public ResponseEntity<ApiResponse<ChangeBranchManagerResponseDTO>> changeBranchManager(
            @RequestBody ChangeBranchManagerRequestDTO requestDTO) {
        log.info("Changing branch manager for branch ID: {}", requestDTO.getBranchId());
        ChangeBranchManagerResponseDTO response = branchManagerService.changeBranchManager(requestDTO);
        return ResponseEntity.ok(new ApiResponse<>(200, "Branch Manager changed successfully", response));
    }

    /**
     * Get employers by branch and role.
     */
    @GetMapping("/employer/by-branch/role/{branchId}")
    @Operation(summary = "Get employers by role", description = "Get employers for a branch filtered by role")
    public ResponseEntity<ApiResponse<List<BranchManagerDTO>>> getEmployersByBranchAndRole(
            @Parameter(description = "Branch ID") @PathVariable Long branchId,
            @Parameter(description = "Role to filter (e.g., OWNER, MANAGER, CASHIER)") @RequestParam String role) {
        log.info("Getting employers for branch ID: {} with role: {}", branchId, role);
        List<BranchManagerDTO> employers = branchManagerService.getEmployersByBranchAndRole(branchId, role);
        return ResponseEntity.ok(new ApiResponse<>(200, "Get Role by branch Id", employers));
    }

    /**
     * Get all cashiers by branch ID.
     */
    @GetMapping("/employer/by-branch-manager/{branchId}")
    @Operation(summary = "Get all cashiers by branch", description = "Get all cashiers for a specific branch")
    public ResponseEntity<ApiResponse<List<BranchManagerDTO>>> getCashiersByBranch(
            @Parameter(description = "Branch ID") @PathVariable Long branchId) {
        log.info("Getting cashiers for branch ID: {}", branchId);
        List<BranchManagerDTO> cashiers = branchManagerService.getCashiersByBranch(branchId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", cashiers));
    }
}
