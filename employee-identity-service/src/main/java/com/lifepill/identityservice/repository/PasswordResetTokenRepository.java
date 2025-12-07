package com.lifepill.identityservice.repository;

import com.lifepill.identityservice.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Repository for PasswordResetToken operations.
 */
@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    
    /**
     * Finds a password reset token by token string.
     *
     * @param token The reset token
     * @return Optional containing the token if found
     */
    Optional<PasswordResetToken> findByToken(String token);
    
    /**
     * Finds all tokens for a specific employer.
     *
     * @param employerId The employer ID
     * @return List of tokens for the employer
     */
    java.util.List<PasswordResetToken> findByEmployerId(Long employerId);
    
    /**
     * Deletes expired tokens.
     *
     * @param now Current timestamp
     */
    void deleteByExpiryDateBefore(LocalDateTime now);
}
