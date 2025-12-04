package com.lifepill.inventoryservice.client;

import com.lifepill.inventoryservice.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Fallback implementation for Branch Service Client
 * Provides default responses when Branch Service is unavailable
 */
@Component
@Slf4j
public class BranchServiceClientFallback implements BranchServiceClient {

    @Override
    public ResponseEntity<ApiResponse<Boolean>> branchExists(Long branchId) {
        log.warn("Fallback: Branch Service unavailable. Cannot verify branch existence for ID: {}", branchId);
        // Return true as fallback to not block operations when branch service is down
        // In production, you might want different behavior based on business requirements
        return ResponseEntity.ok(ApiResponse.success("Fallback response - Branch service unavailable", true));
    }

    @Override
    public ResponseEntity<ApiResponse<BranchDTO>> getBranchById(Long branchId) {
        log.warn("Fallback: Branch Service unavailable. Cannot get branch details for ID: {}", branchId);
        return ResponseEntity.ok(ApiResponse.success(
            "Fallback response - Branch service unavailable",
            new BranchDTO(branchId, "Unknown Branch", "N/A", "N/A", "N/A", false, "N/A")
        ));
    }
}
