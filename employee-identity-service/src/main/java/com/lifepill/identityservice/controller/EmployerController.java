package com.lifepill.identityservice.controller;

import com.lifepill.identityservice.dto.EmployerBankDetailsDTO;
import com.lifepill.identityservice.dto.EmployerDTO;
import com.lifepill.identityservice.dto.EmployerWithBankDetailsDTO;
import com.lifepill.identityservice.dto.request.CreateEmployerRequestDTO;
import com.lifepill.identityservice.dto.request.ForgotPasswordRequestDTO;
import com.lifepill.identityservice.dto.request.ResetPasswordDTO;
import com.lifepill.identityservice.dto.request.UpdateEmployerAccountDTO;
import com.lifepill.identityservice.dto.request.UpdateEmployerBankDetailsDTO;
import com.lifepill.identityservice.dto.request.UpdatePasswordDTO;
import com.lifepill.identityservice.dto.request.UpdatePinDTO;
import com.lifepill.identityservice.entity.enums.Role;
import com.lifepill.identityservice.service.EmployerService;
import com.lifepill.identityservice.service.PasswordService;
import com.lifepill.identityservice.util.StandardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
    private final PasswordService passwordService;

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

    @GetMapping("/get-managers-by-branch")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    @Operation(summary = "Get managers by branch (query param)")
    public ResponseEntity<StandardResponse> getManagersByBranch(@RequestParam Long branchId) {
        log.info("Get managers by branch: {}", branchId);
        List<EmployerDTO> managers = employerService.getManagersByBranch(branchId);
        return ResponseEntity.ok(new StandardResponse(200, "Managers retrieved", managers));
    }

    @GetMapping("/get-by-branch-and-role")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    @Operation(summary = "Get employers by branch and role")
    public ResponseEntity<StandardResponse> getEmployersByBranchAndRole(
            @RequestParam Long branchId,
            @RequestParam Role role) {
        log.info("Get employers by branch {} and role {}", branchId, role);
        List<EmployerDTO> employers = employerService.getEmployersByBranchAndRole(branchId, role);
        return ResponseEntity.ok(new StandardResponse(200, "Employers retrieved", employers));
    }

    @GetMapping("/get-by-id")
    @Operation(summary = "Get employer by ID (query param)")
    public ResponseEntity<StandardResponse> getEmployerByIdParam(@RequestParam Long employerId) {
        log.info("Get employer by ID (param): {}", employerId);
        EmployerDTO employer = employerService.getEmployerById(employerId);
        return ResponseEntity.ok(new StandardResponse(200, "Employer retrieved", employer));
    }

    @PutMapping("/change-role")
    @PreAuthorize("hasRole('OWNER')")
    @Operation(summary = "Change employer role")
    public ResponseEntity<StandardResponse> changeEmployerRole(
            @RequestParam Long employerId,
            @RequestParam Role newRole) {
        log.info("Change role for employer {} to {}", employerId, newRole);
        EmployerDTO employer = employerService.changeEmployerRole(employerId, newRole);
        return ResponseEntity.ok(new StandardResponse(200, "Role changed successfully", employer));
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('OWNER')")
    @Operation(summary = "Create new employer")
    public ResponseEntity<StandardResponse> createEmployer(
            @RequestParam Long branchId,
            @RequestBody CreateEmployerRequestDTO requestDTO) {
        log.info("Create employer for branch: {}", branchId);
        EmployerDTO employer = employerService.createEmployer(branchId, requestDTO);
        return ResponseEntity.status(201).body(new StandardResponse(200, "Employer created successfully", employer));
    }

    @GetMapping("/get-all-employers-bank-details")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    @Operation(summary = "Get all employer bank details", description = "Retrieves bank details for all employers")
    public ResponseEntity<StandardResponse> getAllEmployerBankDetails() {
        log.info("Get all employer bank details");
        List<EmployerBankDetailsDTO> bankDetails = employerService.getAllEmployerBankDetails();
        return ResponseEntity.ok(new StandardResponse(200, "Bank details retrieved", bankDetails));
    }

    @GetMapping("/employers-with-bank-details")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    @Operation(summary = "Get all employers with bank details", description = "Retrieves all employers including their bank account information")
    public ResponseEntity<StandardResponse> getAllEmployersWithBankDetails() {
        log.info("Get all employers with bank details");
        List<EmployerWithBankDetailsDTO> employers = employerService.getAllEmployersWithBankDetails();
        return ResponseEntity.ok(new StandardResponse(200, "Employers with bank details retrieved", employers));
    }

    @GetMapping("/with-bank-details/{employerId}")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'CASHIER')")
    @Operation(summary = "Get employer with bank details by ID", description = "Retrieves complete employer information including bank details")
    public ResponseEntity<StandardResponse> getEmployerWithBankDetails(@PathVariable Long employerId) {
        log.info("Get employer with bank details for ID: {}", employerId);
        EmployerWithBankDetailsDTO employer = employerService.getEmployerWithBankDetails(employerId);
        return ResponseEntity.ok(new StandardResponse(200, "Employer with bank details retrieved", employer));
    }

    @GetMapping("/bank-details/{employerId}")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    @Operation(summary = "Get bank details by employer ID", description = "Retrieves only the bank details for a specific employer")
    public ResponseEntity<StandardResponse> getEmployerBankDetailsById(@PathVariable Long employerId) {
        log.info("Get bank details for employer ID: {}", employerId);
        EmployerBankDetailsDTO bankDetails = employerService.getEmployerBankDetailsById(employerId);
        return ResponseEntity.ok(new StandardResponse(200, "Bank details retrieved", bankDetails));
    }

    @GetMapping("/get-all-employers-by-active-state/{status}")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    @Operation(summary = "Get employers by active status", description = "Filters employers by their active/inactive status")
    public ResponseEntity<StandardResponse> getAllEmployersByActiveStatus(@PathVariable boolean status) {
        log.info("Get all employers by active status: {}", status);
        List<EmployerDTO> employers = employerService.getAllEmployersByActiveStatus(status);
        return ResponseEntity.ok(new StandardResponse(200, "Employers retrieved", employers));
    }

    @PutMapping("/updateEmployerBankAccountDetailsWithId/{employerId}")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    @Operation(summary = "Update employer bank details", description = "Updates bank account information for an employer")
    public ResponseEntity<StandardResponse> updateEmployerBankDetails(
            @PathVariable Long employerId,
            @Valid @RequestBody UpdateEmployerBankDetailsDTO dto) {
        log.info("Update bank details for employer ID: {}", employerId);
        EmployerDTO updatedEmployer = employerService.updateEmployerBankDetails(employerId, dto);
        return ResponseEntity.ok(new StandardResponse(200, "Bank details updated successfully", updatedEmployer));
    }

    @PutMapping("/updateAccountDetails")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'CASHIER')")
    @Operation(summary = "Update employer account details", description = "Updates personal information (name, address, DOB, gender)")
    public ResponseEntity<StandardResponse> updateEmployerAccountDetails(
            @Valid @RequestBody UpdateEmployerAccountDTO dto) {
        log.info("Update account details for employer ID: {}", dto.getEmployerId());
        EmployerDTO updatedEmployer = employerService.updateEmployerAccountDetails(dto);
        return ResponseEntity.ok(new StandardResponse(200, "Account details updated successfully", updatedEmployer));
    }

    @PutMapping("/updateRecentPin")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Update employer PIN", description = "Updates PIN with role hierarchy validation")
    public ResponseEntity<StandardResponse> updateRecentPin(
            Authentication authentication,
            @Valid @RequestBody UpdatePinDTO dto) {
        String requesterEmail = authentication.getName();
        log.info("Update PIN for employer {} requested by {}", dto.getEmployerId(), requesterEmail);
        String message = passwordService.updatePinByEmail(requesterEmail, dto);
        return ResponseEntity.ok(new StandardResponse(200, message, null));
    }

    @PutMapping("/updatePassword")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Update employer password", description = "Updates password with role hierarchy validation")
    public ResponseEntity<StandardResponse> updatePassword(
            Authentication authentication,
            @Valid @RequestBody UpdatePasswordDTO dto) {
        String requesterEmail = authentication.getName();
        log.info("Update password for employer {} requested by {}", dto.getEmployerId(), requesterEmail);
        String message = passwordService.updatePasswordByEmail(requesterEmail, dto);
        return ResponseEntity.ok(new StandardResponse(200, message, null));
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Request password reset", description = "Sends password reset email (public endpoint)")
    public ResponseEntity<StandardResponse> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequestDTO dto) {
        log.info("Forgot password request for email: {}", dto.getEmail());
        String message = passwordService.forgotPassword(dto);
        return ResponseEntity.ok(new StandardResponse(200, message, null));
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset password with token", description = "Resets password using email token (public endpoint)")
    public ResponseEntity<StandardResponse> resetPassword(
            @Valid @RequestBody ResetPasswordDTO dto) {
        log.info("Reset password request with token");
        String message = passwordService.resetPassword(dto);
        return ResponseEntity.ok(new StandardResponse(200, message, null));
    }

    // ========== Image Endpoints ==========

    @PutMapping(value = "/update-employer-image/{employerId}", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'CASHIER')")
    @Operation(summary = "Update employer profile image", description = "Upload a new profile image for an employer. Old image will be deleted from S3.")
    public ResponseEntity<StandardResponse> updateEmployerImage(
            @PathVariable Long employerId,
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        log.info("Update profile image for employer ID: {}", employerId);
        try {
            EmployerDTO updatedEmployer = employerService.updateEmployerImage(employerId, file);
            return ResponseEntity.ok(new StandardResponse(200, "Profile image updated successfully", updatedEmployer));
        } catch (Exception e) {
            log.error("Failed to update profile image for employer ID: {}", employerId, e);
            return ResponseEntity.status(500).body(new StandardResponse(500, "Failed to update image: " + e.getMessage(), null));
        }
    }

    @PostMapping(value = "/save-with-image", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('OWNER')")
    @Operation(summary = "Create employer with image", description = "Create a new employer with profile image uploaded to S3")
    public ResponseEntity<StandardResponse> saveEmployerWithImage(
            @RequestParam Long branchId,
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file,
            @RequestParam("requestDTO") String requestDTOJson) {
        log.info("Create employer with image for branch: {}", branchId);
        try {
            com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
            objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
            com.lifepill.identityservice.dto.request.CreateEmployerRequestDTO requestDTO = 
                    objectMapper.readValue(requestDTOJson, com.lifepill.identityservice.dto.request.CreateEmployerRequestDTO.class);
            
            EmployerDTO createdEmployer = employerService.createEmployerWithImage(branchId, requestDTO, file);
            return ResponseEntity.status(201).body(new StandardResponse(200, "Employer created successfully with image", createdEmployer));
        } catch (Exception e) {
            log.error("Failed to create employer with image for branch: {}", branchId, e);
            return ResponseEntity.status(500).body(new StandardResponse(500, "Failed to create employer: " + e.getMessage(), null));
        }
    }

    @PostMapping(value = "/save-employer-with-image", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('OWNER')")
    @Operation(summary = "Create employer with image (alias)", description = "Alias endpoint for creating employer with profile image")
    public ResponseEntity<StandardResponse> saveEmployerWithImageAlias(
            @RequestParam Long branchId,
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file,
            @RequestParam("requestDTO") String requestDTOJson) {
        return saveEmployerWithImage(branchId, file, requestDTOJson);
    }

    @GetMapping("/view-profile-image/{employerId}")
    @Operation(summary = "View employer profile image", description = "Get the S3 URL of employer's profile image")
    public ResponseEntity<StandardResponse> viewProfileImage(@PathVariable Long employerId) {
        log.info("View profile image for employer ID: {}", employerId);
        try {
            String imageUrl = employerService.getEmployerImageUrl(employerId);
            return ResponseEntity.ok(new StandardResponse(200, "Profile image URL retrieved", imageUrl));
        } catch (Exception e) {
            log.error("Failed to get profile image for employer ID: {}", employerId, e);
            return ResponseEntity.status(404).body(new StandardResponse(404, e.getMessage(), null));
        }
    }

    @GetMapping("/view-image/{employerId}")
    @Operation(summary = "View employer image (alias)", description = "Alias endpoint for getting employer's profile image URL")
    public ResponseEntity<StandardResponse> viewImage(@PathVariable Long employerId) {
        return viewProfileImage(employerId);
    }

    @GetMapping("/profile-photo/{employerId}")
    @Operation(summary = "Get employer profile photo (alias)", description = "Alias endpoint for getting employer's profile image URL")
    public ResponseEntity<StandardResponse> getProfilePhoto(@PathVariable Long employerId) {
        return viewProfileImage(employerId);
    }
}
