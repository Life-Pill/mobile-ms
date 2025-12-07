package com.lifepill.identityservice.service;

/**
 * Service for generating email templates.
 * Decoupled from email sending logic for better separation of concerns.
 */
public interface EmailTemplateService {
    
    /**
     * Generates HTML for password reset email.
     *
     * @param employerName Name of the employer
     * @param resetLink Password reset link with token
     * @return HTML email content
     */
    String generatePasswordResetEmail(String employerName, String resetLink);
}
