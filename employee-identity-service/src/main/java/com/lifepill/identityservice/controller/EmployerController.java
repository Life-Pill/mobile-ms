package com.lifepill.identityservice.controller;

import com.lifepill.identityservice.dto.EmployerDTO;
import com.lifepill.identityservice.entity.enums.Role;
import com.lifepill.identityservice.service.EmployerService;
import com.lifepill.identityservice.util.StandardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for employer management endpoints.
 */
@RestController
@RequestMapping("/lifepill/v1/employer")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Employer Management", description = "Employer CRUD operations")
public class EmployerController {

    private final EmployerService employerService;

    @GetMapping("/{employerId}")
    @Operation(summary = "Get employer by ID")
    public ResponseEntity<StandardResponse> getEmployerById(@PathVariable Long employerId) {
        log.info("Get employer by ID: {}", employerId);
        EmployerDTO employer = employerService.getEmployerById(employerId);
        return ResponseEntity.ok(new StandardResponse(200, "Employer retrieved", employer));
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get employer by email")
    public ResponseEntity<StandardResponse> getEmployerByEmail(@PathVariable String email) {
        log.info("Get employer by email: {}", email);
        EmployerDTO employer = employerService.getEmployerByEmail(email);
        return ResponseEntity.ok(new StandardResponse(200, "Employer retrieved", employer));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    @Operation(summary = "Get all employers")
    public ResponseEntity<StandardResponse> getAllEmployers() {
        log.info("Get all employers");
        List<EmployerDTO> employers = employerService.getAllEmployers();
        return ResponseEntity.ok(new StandardResponse(200, "Employers retrieved", employers));
    }

    @GetMapping("/branch/{branchId}")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    @Operation(summary = "Get employers by branch (path variable)")
    public ResponseEntity<StandardResponse> getEmployersByBranch(@PathVariable Long branchId) {
        log.info("Get employers by branch: {}", branchId);
        List<EmployerDTO> employers = employerService.getEmployersByBranch(branchId);
        return ResponseEntity.ok(new StandardResponse(200, "Employers retrieved", employers));
    }

    @GetMapping("/get-by-branch")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    @Operation(summary = "Get employers by branch (query param)")
    public ResponseEntity<StandardResponse> getEmployersByBranchParam(@RequestParam Long branchId) {
        log.info("Get employers by branch (param): {}", branchId);
        List<EmployerDTO> employers = employerService.getEmployersByBranch(branchId);
        return ResponseEntity.ok(new StandardResponse(200, "Employers retrieved", employers));
    }

    @GetMapping("/count-by-branch")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    @Operation(summary = "Count employers by branch (query param)")
    public ResponseEntity<StandardResponse> countEmployersByBranchParam(@RequestParam Long branchId) {
        log.info("Count employers by branch (param): {}", branchId);
        long count = employerService.countEmployersByBranch(branchId);
        return ResponseEntity.ok(new StandardResponse(200, "Employer count", count));
    }

    @GetMapping("/get-active-by-branch")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    @Operation(summary = "Get active employers by branch (query param)")
    public ResponseEntity<StandardResponse> getActiveEmployersByBranchParam(@RequestParam Long branchId) {
        log.info("Get active employers by branch (param): {}", branchId);
        List<EmployerDTO> employers = employerService.getActiveEmployersByBranch(branchId);
        return ResponseEntity.ok(new StandardResponse(200, "Active employers retrieved", employers));
    }

    @GetMapping("/role/{role}")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    @Operation(summary = "Get employers by role")
    public ResponseEntity<StandardResponse> getEmployersByRole(@PathVariable Role role) {
        log.info("Get employers by role: {}", role);
        List<EmployerDTO> employers = employerService.getEmployersByRole(role);
        return ResponseEntity.ok(new StandardResponse(200, "Employers retrieved", employers));
    }

    @PutMapping("/{employerId}")
    @Operation(summary = "Update employer")
    public ResponseEntity<StandardResponse> updateEmployer(
            @PathVariable Long employerId,
            @RequestBody EmployerDTO employerDTO
    ) {
        log.info("Update employer: {}", employerId);
        EmployerDTO updatedEmployer = employerService.updateEmployer(employerId, employerDTO);
        return ResponseEntity.ok(new StandardResponse(200, "Employer updated", updatedEmployer));
    }

    @DeleteMapping("/{employerId}")
    @PreAuthorize("hasRole('OWNER')")
    @Operation(summary = "Delete employer")
    public ResponseEntity<StandardResponse> deleteEmployer(@PathVariable Long employerId) {
        log.info("Delete employer: {}", employerId);
        employerService.deleteEmployer(employerId);
        return ResponseEntity.ok(new StandardResponse(200, "Employer deleted", null));
    }

    @PutMapping("/{employerId}/branch/{branchId}")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    @Operation(summary = "Update employer's branch assignment")
    public ResponseEntity<StandardResponse> updateEmployerBranch(
            @PathVariable Long employerId,
            @PathVariable Long branchId
    ) {
        log.info("Update employer {} branch to {}", employerId, branchId);
        EmployerDTO updatedEmployer = employerService.updateEmployerBranch(employerId, branchId);
        return ResponseEntity.ok(new StandardResponse(200, "Employer branch updated", updatedEmployer));
    }

    @GetMapping("/branch/{branchId}/count")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    @Operation(summary = "Count employers in branch")
    public ResponseEntity<StandardResponse> countEmployersByBranch(@PathVariable Long branchId) {
        log.info("Count employers in branch: {}", branchId);
        long count = employerService.countEmployersByBranch(branchId);
        return ResponseEntity.ok(new StandardResponse(200, "Employer count", count));
    }

    @GetMapping("/branch/{branchId}/active")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    @Operation(summary = "Get active employers in branch")
    public ResponseEntity<StandardResponse> getActiveEmployersByBranch(@PathVariable Long branchId) {
        log.info("Get active employers in branch: {}", branchId);
        List<EmployerDTO> employers = employerService.getActiveEmployersByBranch(branchId);
        return ResponseEntity.ok(new StandardResponse(200, "Active employers retrieved", employers));
    }
}
