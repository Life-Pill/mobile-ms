package com.lifepill.branchservice.client;

import com.lifepill.branchservice.dto.ApiResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Feign client for Identity Service communication.
 * Used to get employer information for a branch.
 * Uses Circuit Breaker and Retry patterns for resilience.
 */
@FeignClient(
    name = "IDENTITY-SERVICE",
    fallback = IdentityServiceClientFallback.class
)
public interface IdentityServiceClient {

    /**
     * Get employers by branch ID
     * @param branchId the branch ID
     * @return list of employers for the branch
     */
    @GetMapping("/lifepill/v1/employer/get-by-branch")
    @CircuitBreaker(name = "identityService", fallbackMethod = "getEmployersByBranchFallback")
    @Retry(name = "identityService")
    ResponseEntity<ApiResponse<List<EmployerDTO>>> getEmployersByBranch(@RequestParam Long branchId);

    /**
     * Get employer count by branch ID
     * @param branchId the branch ID
     * @return count of employers for the branch
     */
    @GetMapping("/lifepill/v1/employer/count-by-branch")
    @CircuitBreaker(name = "identityService", fallbackMethod = "countEmployersByBranchFallback")
    @Retry(name = "identityService")
    ResponseEntity<ApiResponse<Long>> countEmployersByBranch(@RequestParam Long branchId);

    /**
     * Get active employers by branch ID
     * @param branchId the branch ID
     * @return list of active employers for the branch
     */
    @GetMapping("/lifepill/v1/employer/get-active-by-branch")
    @CircuitBreaker(name = "identityService", fallbackMethod = "getActiveEmployersByBranchFallback")
    @Retry(name = "identityService")
    ResponseEntity<ApiResponse<List<EmployerDTO>>> getActiveEmployersByBranch(@RequestParam Long branchId);

    /**
     * DTO for Employer data from Identity Service
     */
    record EmployerDTO(
        Long employerId,
        String employerNicName,
        String employerFirstName,
        String employerLastName,
        String employerEmail,
        String employerPhone,
        String employerAddress,
        Double employerSalary,
        String employerNic,
        boolean isActiveStatus,
        String gender,
        String dateOfBirth,
        String role,
        Long branchId,
        String profileImageUrl
    ) {}
}
