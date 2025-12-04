package com.lifepill.branchservice.client;

import com.lifepill.branchservice.dto.ApiResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Feign client for Inventory Service communication
 * Uses Circuit Breaker and Retry patterns for resilience
 */
@FeignClient(
    name = "INVENTORY-SERVICE",
    fallback = InventoryServiceClientFallback.class
)
public interface InventoryServiceClient {

    /**
     * Get items by branch ID
     * @param branchId the branch ID
     * @return list of items for the branch
     */
    @GetMapping("/lifepill/v1/item/by-branch/{branchId}")
    @CircuitBreaker(name = "inventoryService", fallbackMethod = "getItemsByBranchFallback")
    @Retry(name = "inventoryService")
    ResponseEntity<ApiResponse<List<ItemDTO>>> getItemsByBranch(@PathVariable Long branchId);

    /**
     * Get item count by branch ID
     * @param branchId the branch ID
     * @return count of items for the branch
     */
    @GetMapping("/lifepill/v1/item/count-by-branch")
    @CircuitBreaker(name = "inventoryService", fallbackMethod = "countItemsByBranchFallback")
    @Retry(name = "inventoryService")
    ResponseEntity<ApiResponse<Long>> countItemsByBranch(@RequestParam Long branchId);

    /**
     * Get low stock items by branch ID
     * @param branchId the branch ID
     * @param threshold the quantity threshold
     * @return list of low stock items
     */
    @GetMapping("/lifepill/v1/item/low-stock/{branchId}")
    @CircuitBreaker(name = "inventoryService", fallbackMethod = "getLowStockItemsFallback")
    @Retry(name = "inventoryService")
    ResponseEntity<ApiResponse<List<ItemDTO>>> getLowStockItems(
        @PathVariable Long branchId,
        @RequestParam(defaultValue = "10") Double threshold
    );

    /**
     * DTO for Item data from Inventory Service
     */
    record ItemDTO(
        Long itemId,
        String itemName,
        Double sellingPrice,
        String itemBarCode,
        Double itemQuantity,
        boolean stock,
        String categoryName,
        String supplierName
    ) {}
}
