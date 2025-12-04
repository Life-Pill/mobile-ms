package com.lifepill.identityservice.repository;

import com.lifepill.identityservice.entity.Employer;
import com.lifepill.identityservice.entity.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Employer entity operations.
 */
@Repository
public interface EmployerRepository extends JpaRepository<Employer, Long> {

    /**
     * Finds an employer by their email.
     *
     * @param email The employer's email
     * @return Optional containing the employer if found
     */
    Optional<Employer> findByEmployerEmail(String email);

    /**
     * Checks if an employer exists by email.
     *
     * @param email The employer's email
     * @return true if an employer with this email exists
     */
    boolean existsByEmployerEmail(String email);

    /**
     * Checks if an employer with the given email exists.
     *
     * @param email The employer's email
     * @return true if an employer with this email exists
     */
    boolean existsAllByEmployerEmail(String email);

    /**
     * Finds all employers by branch ID.
     *
     * @param branchId The branch ID
     * @return List of employers in the branch
     */
    List<Employer> findByBranchId(Long branchId);

    /**
     * Finds all employers by role.
     *
     * @param role The role to filter by
     * @return List of employers with the specified role
     */
    List<Employer> findByRole(Role role);

    /**
     * Finds all employers by branch ID and role.
     *
     * @param branchId The branch ID
     * @param role The role to filter by
     * @return List of employers matching the criteria
     */
    List<Employer> findByBranchIdAndRole(Long branchId, Role role);

    /**
     * Counts employers by branch ID.
     *
     * @param branchId The branch ID
     * @return Count of employers in the branch
     */
    long countByBranchId(Long branchId);

    /**
     * Finds all active employers.
     *
     * @return List of active employers
     */
    List<Employer> findByIsActiveStatusTrue();

    /**
     * Finds employers by branch ID and active status.
     *
     * @param branchId The branch ID
     * @param isActiveStatus The active status
     * @return List of employers matching the criteria
     */
    List<Employer> findByBranchIdAndIsActiveStatus(Long branchId, boolean isActiveStatus);
}
