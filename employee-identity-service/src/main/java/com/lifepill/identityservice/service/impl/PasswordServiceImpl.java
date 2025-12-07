package com.lifepill.identityservice.service.impl;

import com.lifepill.identityservice.dto.request.ForgotPasswordRequestDTO;
import com.lifepill.identityservice.dto.request.ResetPasswordDTO;
import com.lifepill.identityservice.dto.request.UpdatePasswordDTO;
import com.lifepill.identityservice.dto.request.UpdatePinDTO;
import com.lifepill.identityservice.entity.Employer;
import com.lifepill.identityservice.entity.PasswordResetToken;
import com.lifepill.identityservice.exception.NotFoundException;
import com.lifepill.identityservice.repository.EmployerRepository;
import com.lifepill.identityservice.repository.PasswordResetTokenRepository;
import com.lifepill.identityservice.service.EmailService;
import com.lifepill.identityservice.service.PasswordService;
import com.lifepill.identityservice.util.HierarchyValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Implementation of PasswordService with role hierarchy validation.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PasswordServiceImpl implements PasswordService {
    
    private final EmployerRepository employerRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public String updatePin(Long requesterId, UpdatePinDTO dto) {
        log.info("Updating PIN for employer {} requested by {}", dto.getEmployerId(), requesterId);
        
        // Get requester and target employers
        Employer requester = employerRepository.findById(requesterId)
                .orElseThrow(() -> new NotFoundException("Requester not found"));
        
        Employer target = employerRepository.findById(dto.getEmployerId())
                .orElseThrow(() -> new NotFoundException("Target employer not found with ID: " + dto.getEmployerId()));
        
        // Validate hierarchy
        if (!HierarchyValidator.canUpdate(requester.getRole(), target.getRole(), requesterId, dto.getEmployerId())) {
            String message = HierarchyValidator.getHierarchyViolationMessage(requester.getRole(), target.getRole());
            log.warn("Hierarchy violation: {} (role: {}) tried to update PIN for {} (role: {})", 
                    requesterId, requester.getRole(), dto.getEmployerId(), target.getRole());
            throw new AccessDeniedException(message);
        }
        
        // Update PIN
        target.setPin(dto.getPin());
        employerRepository.save(target);
        
        log.info("Successfully updated PIN for employer {}", dto.getEmployerId());
        return "PIN updated successfully";
    }
    
    @Override
    public String updatePassword(Long requesterId, UpdatePasswordDTO dto) {
        log.info("Updating password for employer {} requested by {}", dto.getEmployerId(), requesterId);
        
        // Get requester and target employers
        Employer requester = employerRepository.findById(requesterId)
                .orElseThrow(() -> new NotFoundException("Requester not found"));
        
        Employer target = employerRepository.findById(dto.getEmployerId())
                .orElseThrow(() -> new NotFoundException("Target employer not found with ID: " + dto.getEmployerId()));
        
        // Validate hierarchy
        if (!HierarchyValidator.canUpdate(requester.getRole(), target.getRole(), requesterId, dto.getEmployerId())) {
            String message = HierarchyValidator.getHierarchyViolationMessage(requester.getRole(), target.getRole());
            log.warn("Hierarchy violation: {} (role: {}) tried to update password for {} (role: {})", 
                    requesterId, requester.getRole(), dto.getEmployerId(), target.getRole());
            throw new AccessDeniedException(message);
        }
        
        // Hash and update password
        String hashedPassword = passwordEncoder.encode(dto.getEmployerPassword());
        target.setEmployerPassword(hashedPassword);
        employerRepository.save(target);
        
        log.info("Successfully updated password for employer {}", dto.getEmployerId());
        return "Password updated successfully";
    }
    
    @Override
    public String updatePinByEmail(String requesterEmail, UpdatePinDTO dto) {
        log.info("Updating PIN for employer {} requested by email: {}", dto.getEmployerId(), requesterEmail);
        
        // Get requester by email
        Employer requester = employerRepository.findByEmployerEmail(requesterEmail)
                .orElseThrow(() -> new NotFoundException("Requester not found with email: " + requesterEmail));
        
        // Delegate to ID-based method
        return updatePin(requester.getEmployerId(), dto);
    }
    
    @Override
    public String updatePasswordByEmail(String requesterEmail, UpdatePasswordDTO dto) {
        log.info("Updating password for employer {} requested by email: {}", dto.getEmployerId(), requesterEmail);
        
        // Get requester by email
        Employer requester = employerRepository.findByEmployerEmail(requesterEmail)
                .orElseThrow(() -> new NotFoundException("Requester not found with email: " + requesterEmail));
        
        // Delegate to ID-based method
        return updatePassword(requester.getEmployerId(), dto);
    }
    
    @Override
    public String forgotPassword(ForgotPasswordRequestDTO dto) {
        log.info("Forgot password request for email: {}", dto.getEmail());
        
        // Find employer by email
        Employer employer = employerRepository.findByEmployerEmail(dto.getEmail())
                .orElseThrow(() -> new NotFoundException("No account found with that email address"));
        
        // Generate unique token
        String token = UUID.randomUUID().toString();
        
        // Create password reset token entity
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .employerId(employer.getEmployerId())
                .expiryDate(LocalDateTime.now().plusHours(1)) // 1 hour expiry
                .used(false)
                .createdAt(LocalDateTime.now())
                .build();
        
        tokenRepository.save(resetToken);
        
        // Send email
        String employerName = employer.getEmployerFirstName() + " " + employer.getEmployerLastName();
        emailService.sendPasswordResetEmail(employer.getEmployerEmail(), employerName, token);
        
        log.info("Password reset email sent to: {}", dto.getEmail());
        return "Password reset link has been sent to your email";
    }
    
    @Override
    public String resetPassword(ResetPasswordDTO dto) {
        log.info("Reset password request with token");
        
        // Find token
        PasswordResetToken resetToken = tokenRepository.findByToken(dto.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid or expired reset token"));
        
        // Validate token
        if (resetToken.getUsed()) {
            throw new RuntimeException("This reset link has already been used");
        }
        
        if (resetToken.isExpired()) {
            throw new RuntimeException("This reset link has expired. Please request a new one");
        }
        
        // Get employer
        Employer employer = employerRepository.findById(resetToken.getEmployerId())
                .orElseThrow(() -> new NotFoundException("Employer not found"));
        
        // Update password
        String hashedPassword = passwordEncoder.encode(dto.getNewPassword());
        employer.setEmployerPassword(hashedPassword);
        employerRepository.save(employer);
        
        // Mark token as used
        resetToken.setUsed(true);
        tokenRepository.save(resetToken);
        
        log.info("Password successfully reset for employer {}", employer.getEmployerId());
        return "Password has been reset successfully";
    }
}
