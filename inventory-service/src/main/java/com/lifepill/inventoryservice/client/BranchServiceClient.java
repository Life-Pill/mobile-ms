package com.lifepill.inventoryservice.client;

import com.lifepill.inventoryservice.dto.ApiResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Feign client for Branch Service communication
 * Uses Circuit Breaker and Retry patterns for resilience
 */
@FeignClient(
    name = "BRANCH-SERVICE",
    fallback = BranchServiceClientFallback.class
)
public interface BranchServiceClient {

    /**
     * Check if a branch exists by ID
     * @param branchId the branch ID to check
     * @return true if branch exists, false otherwise
     */
    @GetMapping("/lifepill/v1/branch/exists")
    @CircuitBreaker(name = "branchService", fallbackMethod = "branchExistsFallback")
    @Retry(name = "branchService")
    ResponseEntity<ApiResponse<Boolean>> branchExists(@RequestParam("branchId") Long branchId);

    /**
     * Get branch details by ID
     * @param branchId the branch ID
     * @return branch details
     */
    @GetMapping("/lifepill/v1/branch/get-by-id")
    @CircuitBreaker(name = "branchService", fallbackMethod = "getBranchByIdFallback")
    @Retry(name = "branchService")
    ResponseEntity<ApiResponse<BranchDTO>> getBranchById(@RequestParam("branchId") Long branchId);

    /**
     * DTO for Branch data from Branch Service
     */
    record BranchDTO(
        Long branchId,
        String branchName,
        String branchAddress,
        String branchContact,
        String branchEmail,
        boolean branchStatus,
        String branchLocation
    ) {}
}
