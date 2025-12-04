package com.lifepill.possystem.client;

import com.lifepill.possystem.client.dto.MicroserviceApiResponse;
import com.lifepill.possystem.client.dto.MicroserviceEmployeeDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Feign client for Identity Service (Employee Management) communication
 * Uses Circuit Breaker and Retry patterns for resilience
 */
@FeignClient(
    name = "IDENTITY-SERVICE",
    fallback = IdentityServiceClientFallback.class
)
public interface IdentityServiceClient {

    /**
     * Get employee by ID
     * @param employerId the employee ID
     * @return the employee
     */
    @GetMapping("/lifepill/v1/employer/{employerId}")
    @CircuitBreaker(name = "identityService", fallbackMethod = "getEmployeeByIdFallback")
    @Retry(name = "identityService")
    ResponseEntity<MicroserviceApiResponse<MicroserviceEmployeeDTO>> getEmployeeById(@PathVariable Long employerId);

    /**
     * Get employees by branch ID
     * @param branchId the branch ID
     * @return list of employees for the branch
     */
    @GetMapping("/lifepill/v1/employer/by-branch/{branchId}")
    @CircuitBreaker(name = "identityService", fallbackMethod = "getEmployeesByBranchFallback")
    @Retry(name = "identityService")
    ResponseEntity<MicroserviceApiResponse<List<MicroserviceEmployeeDTO>>> getEmployeesByBranch(@PathVariable Long branchId);

    /**
     * Get manager for a branch
     * @param branchId the branch ID
     * @return the manager
     */
    @GetMapping("/lifepill/v1/employer/manager/branch/{branchId}")
    @CircuitBreaker(name = "identityService", fallbackMethod = "getManagerByBranchFallback")
    @Retry(name = "identityService")
    ResponseEntity<MicroserviceApiResponse<MicroserviceEmployeeDTO>> getManagerByBranch(@PathVariable Long branchId);

    /**
     * Check if employee exists
     * @param employerId the employee ID
     * @return true if employee exists
     */
    @GetMapping("/lifepill/v1/employer/{employerId}/exists")
    @CircuitBreaker(name = "identityService", fallbackMethod = "employeeExistsFallback")
    @Retry(name = "identityService")
    ResponseEntity<MicroserviceApiResponse<Boolean>> employeeExists(@PathVariable Long employerId);

    /**
     * Count employees by branch
     * @param branchId the branch ID
     * @return count of employees
     */
    @GetMapping("/lifepill/v1/employer/count-by-branch")
    @CircuitBreaker(name = "identityService", fallbackMethod = "countEmployeesByBranchFallback")
    @Retry(name = "identityService")
    ResponseEntity<MicroserviceApiResponse<Long>> countEmployeesByBranch(@RequestParam Long branchId);

    /**
     * Get employee by email for authentication
     * @param email the employee email
     * @return the employee
     */
    @GetMapping("/lifepill/v1/employer/by-email/{email}")
    @CircuitBreaker(name = "identityService", fallbackMethod = "getEmployeeByEmailFallback")
    @Retry(name = "identityService")
    ResponseEntity<MicroserviceApiResponse<MicroserviceEmployeeDTO>> getEmployeeByEmail(@PathVariable String email);
}
