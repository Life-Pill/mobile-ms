package com.lifepill.possystem.client;

import com.lifepill.possystem.client.dto.MicroserviceApiResponse;
import com.lifepill.possystem.client.dto.MicroserviceBranchDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

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
     * Get all branches
     * @return list of all branches
     */
    @GetMapping("/lifepill/v1/branch/get-all")
    @CircuitBreaker(name = "branchService", fallbackMethod = "getAllBranchesFallback")
    @Retry(name = "branchService")
    ResponseEntity<MicroserviceApiResponse<List<MicroserviceBranchDTO>>> getAllBranches();

    /**
     * Get branch by ID
     * @param branchId the branch ID
     * @return the branch
     */
    @GetMapping("/lifepill/v1/branch/{branchId}")
    @CircuitBreaker(name = "branchService", fallbackMethod = "getBranchByIdFallback")
    @Retry(name = "branchService")
    ResponseEntity<MicroserviceApiResponse<MicroserviceBranchDTO>> getBranchById(@PathVariable Long branchId);

    /**
     * Check if branch exists
     * @param branchId the branch ID
     * @return true if branch exists
     */
    @GetMapping("/lifepill/v1/branch/{branchId}/exists")
    @CircuitBreaker(name = "branchService", fallbackMethod = "branchExistsFallback")
    @Retry(name = "branchService")
    ResponseEntity<MicroserviceApiResponse<Boolean>> branchExists(@PathVariable Long branchId);

    /**
     * Get count of all branches
     * @return count of branches
     */
    @GetMapping("/lifepill/v1/branch/count")
    @CircuitBreaker(name = "branchService", fallbackMethod = "countBranchesFallback")
    @Retry(name = "branchService")
    ResponseEntity<MicroserviceApiResponse<Long>> countBranches();
}
