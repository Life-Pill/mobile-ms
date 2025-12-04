package com.lifepill.branchservice.client;

import com.lifepill.branchservice.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Fallback implementation for Inventory Service Client
 * Provides default responses when Inventory Service is unavailable
 */
@Component
@Slf4j
public class InventoryServiceClientFallback implements InventoryServiceClient {

    @Override
    public ResponseEntity<ApiResponse<List<ItemDTO>>> getItemsByBranch(Long branchId) {
        log.warn("Fallback: Inventory Service unavailable. Cannot get items for branch ID: {}", branchId);
        return ResponseEntity.ok(ApiResponse.success(
            "Fallback response - Inventory service unavailable",
            Collections.emptyList()
        ));
    }

    @Override
    public ResponseEntity<ApiResponse<Long>> countItemsByBranch(Long branchId) {
        log.warn("Fallback: Inventory Service unavailable. Cannot count items for branch ID: {}", branchId);
        return ResponseEntity.ok(ApiResponse.success(
            "Fallback response - Inventory service unavailable",
            0L
        ));
    }

    @Override
    public ResponseEntity<ApiResponse<List<ItemDTO>>> getLowStockItems(Long branchId, Double threshold) {
        log.warn("Fallback: Inventory Service unavailable. Cannot get low stock items for branch ID: {}", branchId);
        return ResponseEntity.ok(ApiResponse.success(
            "Fallback response - Inventory service unavailable",
            Collections.emptyList()
        ));
    }
}
