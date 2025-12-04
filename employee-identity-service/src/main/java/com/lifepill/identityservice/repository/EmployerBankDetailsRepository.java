package com.lifepill.identityservice.repository;

import com.lifepill.identityservice.entity.EmployerBankDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for EmployerBankDetails entity operations.
 */
@Repository
public interface EmployerBankDetailsRepository extends JpaRepository<EmployerBankDetails, Long> {

    /**
     * Finds bank details by employer ID.
     *
     * @param employerId The employer ID
     * @return Optional containing the bank details if found
     */
    Optional<EmployerBankDetails> findByEmployerId(Long employerId);

    /**
     * Finds all bank details by bank name.
     *
     * @param bankName The bank name
     * @return List of bank details for the specified bank
     */
    List<EmployerBankDetails> findByBankName(String bankName);

    /**
     * Checks if bank details exist for an employer.
     *
     * @param employerId The employer ID
     * @return true if bank details exist for the employer
     */
    boolean existsByEmployerId(Long employerId);
}
