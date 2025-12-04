package com.lifepill.possystem.client;

import com.lifepill.possystem.client.dto.MicroserviceApiResponse;
import com.lifepill.possystem.client.dto.MicroserviceItemDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * Get item by ID
     * @param itemId the item ID
     * @return the item
     */
    @GetMapping("/lifepill/v1/item/{itemId}")
    @CircuitBreaker(name = "inventoryService", fallbackMethod = "getItemByIdFallback")
    @Retry(name = "inventoryService")
    ResponseEntity<MicroserviceApiResponse<MicroserviceItemDTO>> getItemById(@PathVariable Long itemId);

    /**
     * Get items by branch ID
     * @param branchId the branch ID
     * @return list of items for the branch
     */
    @GetMapping("/lifepill/v1/item/by-branch/{branchId}")
    @CircuitBreaker(name = "inventoryService", fallbackMethod = "getItemsByBranchFallback")
    @Retry(name = "inventoryService")
    ResponseEntity<MicroserviceApiResponse<List<MicroserviceItemDTO>>> getItemsByBranch(@PathVariable Long branchId);

    /**
     * Update stock for an item
     * @param itemId the item ID
     * @param quantityChange the quantity change (positive to add, negative to deduct)
     * @return the updated item
     */
    @PutMapping("/lifepill/v1/item/{itemId}/stock")
    @CircuitBreaker(name = "inventoryService", fallbackMethod = "updateStockFallback")
    @Retry(name = "inventoryService")
    ResponseEntity<MicroserviceApiResponse<MicroserviceItemDTO>> updateStock(
        @PathVariable Long itemId,
        @RequestParam Double quantityChange
    );

    /**
     * Check if item has sufficient stock
     * @param itemId the item ID
     * @param quantity the required quantity
     * @return true if sufficient stock exists
     */
    @GetMapping("/lifepill/v1/item/{itemId}/check-stock")
    @CircuitBreaker(name = "inventoryService", fallbackMethod = "checkStockFallback")
    @Retry(name = "inventoryService")
    ResponseEntity<MicroserviceApiResponse<Boolean>> checkStock(
        @PathVariable Long itemId,
        @RequestParam Double quantity
    );
}
