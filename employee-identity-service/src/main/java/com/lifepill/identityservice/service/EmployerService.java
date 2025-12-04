package com.lifepill.identityservice.service;

import com.lifepill.identityservice.dto.EmployerDTO;
import com.lifepill.identityservice.dto.request.RegisterRequestDTO;
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
}
