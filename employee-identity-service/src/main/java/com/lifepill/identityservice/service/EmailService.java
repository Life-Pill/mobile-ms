package com.lifepill.identityservice.service;

/**
 * Service for sending emails.
 */
public interface EmailService {
    
    /**
     * Sends a password reset email.
     *
     * @param toEmail Recipient email address
     * @param employerName Name of the employer
     * @param resetToken Reset token
     */
    void sendPasswordResetEmail(String toEmail, String employerName, String resetToken);
}
