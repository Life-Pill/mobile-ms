package com.lifepill.branchservice.client;

import com.lifepill.branchservice.dto.ApiResponse;
import com.lifepill.branchservice.dto.manager.BranchManagerDTO;
import com.lifepill.branchservice.dto.manager.UpdateManagerRequestDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * Get employers by branch ID and role
     * @param branchId the branch ID
     * @param role the role to filter by
     * @return list of employers with specified role
     */
    @GetMapping("/lifepill/v1/employer/get-by-branch-and-role")
    @CircuitBreaker(name = "identityService", fallbackMethod = "getEmployersByBranchAndRoleFallback")
    @Retry(name = "identityService")
    ResponseEntity<ApiResponse<List<BranchManagerDTO>>> getEmployersByBranchAndRole(
            @RequestParam Long branchId, 
            @RequestParam String role);

    /**
     * Get managers (MANAGER role) by branch ID
     * @param branchId the branch ID
     * @return list of managers for the branch
     */
    @GetMapping("/lifepill/v1/employer/get-managers-by-branch")
    @CircuitBreaker(name = "identityService", fallbackMethod = "getManagersByBranchFallback")
    @Retry(name = "identityService")
    ResponseEntity<ApiResponse<List<BranchManagerDTO>>> getManagersByBranch(@RequestParam Long branchId);

    /**
     * Get employer by ID
     * @param employerId the employer ID
     * @return employer details
     */
    @GetMapping("/lifepill/v1/employer/get-by-id")
    @CircuitBreaker(name = "identityService", fallbackMethod = "getEmployerByIdFallback")
    @Retry(name = "identityService")
    ResponseEntity<ApiResponse<BranchManagerDTO>> getEmployerById(@RequestParam Long employerId);

    /**
     * Update employer details
     * @param employerId the employer ID
     * @param updateDTO the update data
     * @return updated employer
     */
    @PutMapping("/lifepill/v1/employer/update")
    @CircuitBreaker(name = "identityService", fallbackMethod = "updateEmployerFallback")
    @Retry(name = "identityService")
    ResponseEntity<ApiResponse<BranchManagerDTO>> updateEmployer(
            @RequestParam Long employerId,
            @RequestBody UpdateManagerRequestDTO updateDTO);

    /**
     * Change employer role
     * @param employerId the employer ID
     * @param newRole the new role
     * @return updated employer
     */
    @PutMapping("/lifepill/v1/employer/change-role")
    @CircuitBreaker(name = "identityService", fallbackMethod = "changeEmployerRoleFallback")
    @Retry(name = "identityService")
    ResponseEntity<ApiResponse<BranchManagerDTO>> changeEmployerRole(
            @RequestParam Long employerId,
            @RequestParam String newRole);

    /**
     * Create a new employer
     * @param branchId the branch ID
     * @param createDTO the employer data
     * @return created employer
     */
    @PostMapping("/lifepill/v1/employer/create")
    @CircuitBreaker(name = "identityService", fallbackMethod = "createEmployerFallback")
    @Retry(name = "identityService")
    ResponseEntity<ApiResponse<BranchManagerDTO>> createEmployer(
            @RequestParam Long branchId,
            @RequestBody UpdateManagerRequestDTO createDTO);

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

