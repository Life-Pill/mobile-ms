package com.lifepill.identityservice.service;

import com.lifepill.identityservice.dto.EmployerBankDetailsDTO;
import com.lifepill.identityservice.dto.EmployerDTO;
import com.lifepill.identityservice.dto.EmployerWithBankDetailsDTO;
import com.lifepill.identityservice.dto.request.CreateEmployerRequestDTO;
import com.lifepill.identityservice.dto.request.RegisterRequestDTO;
import com.lifepill.identityservice.dto.request.UpdateEmployerAccountDTO;
import com.lifepill.identityservice.dto.request.UpdateEmployerBankDetailsDTO;
import com.lifepill.identityservice.entity.enums.Role;

import java.util.List;

/**
 * Service interface for employer management operations.
 */
public interface EmployerService {

    /**
     * Gets an employer by ID.
     *
     * @param employerId The employer ID
     * @return The employer DTO
     */
    EmployerDTO getEmployerById(Long employerId);

    /**
     * Gets an employer by email.
     *
     * @param email The employer email
     * @return The employer DTO
     */
    EmployerDTO getEmployerByEmail(String email);

    /**
     * Gets all employers.
     *
     * @return List of all employers
     */
    List<EmployerDTO> getAllEmployers();

    /**
     * Gets all employers by branch ID.
     *
     * @param branchId The branch ID
     * @return List of employers in the branch
     */
    List<EmployerDTO> getEmployersByBranch(Long branchId);

    /**
     * Gets all employers by role.
     *
     * @param role The role to filter by
     * @return List of employers with the role
     */
    List<EmployerDTO> getEmployersByRole(Role role);

    /**
     * Updates an employer.
     *
     * @param employerId The employer ID
     * @param employerDTO The updated employer data
     * @return The updated employer DTO
     */
    EmployerDTO updateEmployer(Long employerId, EmployerDTO employerDTO);

    /**
     * Deletes an employer.
     *
     * @param employerId The employer ID
     */
    void deleteEmployer(Long employerId);

    /**
     * Updates employer's branch assignment.
     *
     * @param employerId The employer ID
     * @param branchId The new branch ID
     * @return The updated employer DTO
     */
    EmployerDTO updateEmployerBranch(Long employerId, Long branchId);

    /**
     * Counts employers in a branch.
     *
     * @param branchId The branch ID
     * @return Count of employers
     */
    long countEmployersByBranch(Long branchId);

    /**
     * Gets active employers by branch.
     *
     * @param branchId The branch ID
     * @return List of active employers
     */
    List<EmployerDTO> getActiveEmployersByBranch(Long branchId);

    /**
     * Gets managers (MANAGER role) by branch.
     *
     * @param branchId The branch ID
     * @return List of managers in the branch
     */
    List<EmployerDTO> getManagersByBranch(Long branchId);

    /**
     * Gets employers by branch and role.
     *
     * @param branchId The branch ID
     * @param role The role to filter by
     * @return List of employers with the role in the branch
     */
    List<EmployerDTO> getEmployersByBranchAndRole(Long branchId, Role role);

    /**
     * Changes an employer's role.
     *
     * @param employerId The employer ID
     * @param newRole The new role
     * @return The updated employer DTO
     */
    EmployerDTO changeEmployerRole(Long employerId, Role newRole);

    /**
     * Creates a new employer.
     *
     * @param branchId The branch ID
     * @param requestDTO The employer creation data
     * @return The created employer DTO
     */
    EmployerDTO createEmployer(Long branchId, CreateEmployerRequestDTO requestDTO);

    /**
     * Gets all employer bank details.
     *
     * @return List of all bank details
     */
    List<EmployerBankDetailsDTO> getAllEmployerBankDetails();

    /**
     * Gets all employers with their bank details.
     *
     * @return List of employers with bank details
     */
    List<EmployerWithBankDetailsDTO> getAllEmployersWithBankDetails();

    /**
     * Gets employer with bank details by ID.
     *
     * @param employerId The employer ID
     * @return Employer with bank details
     */
    EmployerWithBankDetailsDTO getEmployerWithBankDetails(Long employerId);

    /**
     * Gets bank details for a specific employer.
     *
     * @param employerId The employer ID
     * @return Bank details DTO
     */
    EmployerBankDetailsDTO getEmployerBankDetailsById(Long employerId);

    /**
     * Gets all employers filtered by active status.
     *
     * @param status The active status to filter by
     * @return List of employers with the specified status
     */
    List<EmployerDTO> getAllEmployersByActiveStatus(boolean status);

    /**
     * Updates employer bank details.
     *
     * @param employerId The employer ID
     * @param dto The updated bank details
     * @return Updated employer DTO
     */
    EmployerDTO updateEmployerBankDetails(Long employerId, UpdateEmployerBankDetailsDTO dto);

    /**
     * Updates employer account details.
     *
     * @param dto The updated account information
     * @return Updated employer DTO
     */
    EmployerDTO updateEmployerAccountDetails(UpdateEmployerAccountDTO dto);

    /**
     * Updates employer profile image.
     *
     * @param employerId The employer ID
     * @param image The image file to upload
     * @return Updated employer DTO with new image URL
     */
    EmployerDTO updateEmployerImage(Long employerId, org.springframework.web.multipart.MultipartFile image);

    /**
     * Gets employer profile image URL.
     *
     * @param employerId The employer ID
     * @return The profile image URL
     */
    String getEmployerImageUrl(Long employerId);

    /**
     * Creates a new employer with profile image.
     *
     * @param branchId The branch ID
     * @param requestDTO The employer creation data
     * @param image The profile image file
     * @return The created employer DTO
     */
    EmployerDTO createEmployerWithImage(Long branchId, CreateEmployerRequestDTO requestDTO, org.springframework.web.multipart.MultipartFile image);
}

