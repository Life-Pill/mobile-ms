package com.lifepill.identityservice.client;

import com.lifepill.identityservice.client.dto.MicroserviceApiResponse;
import com.lifepill.identityservice.client.fallback.BranchServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign client for communicating with the Branch Service.
 */
@FeignClient(
        name = "BRANCH-SERVICE",
        fallback = BranchServiceFallback.class
)
public interface BranchServiceClient {

    /**
     * Checks if a branch exists.
     *
     * @param branchId The ID of the branch
     * @return Response indicating whether the branch exists
     */
    @GetMapping("/lifepill/v1/branch/{branchId}/exists")
    ResponseEntity<MicroserviceApiResponse<Boolean>> branchExists(@PathVariable Long branchId);
}
