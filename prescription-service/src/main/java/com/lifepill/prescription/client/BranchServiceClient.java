package com.lifepill.prescription.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign client for communicating with Branch Service.
 */
@FeignClient(name = "BRANCH-SERVICE", fallback = BranchServiceClientFallback.class)
public interface BranchServiceClient {
    
    @GetMapping("/lifepill/v1/branch/{branchId}")
    BranchApiResponse<BranchDetailsDTO> getBranchById(@PathVariable("branchId") Long branchId);
}
