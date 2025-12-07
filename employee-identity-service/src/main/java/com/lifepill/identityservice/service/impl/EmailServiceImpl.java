package com.lifepill.identityservice.service.impl;

import com.lifepill.identityservice.service.EmailService;
import com.lifepill.identityservice.service.EmailTemplateService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Implementation of EmailService for sending emails.
 * Uses EmailTemplateService for HTML template generation.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {
    
    private final JavaMailSender mailSender;
    private final EmailTemplateService emailTemplateService;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    @Value("${app.password-reset-url:http://localhost:9191/reset-password}")
    private String passwordResetBaseUrl;
    
    @Override
    @Async
    public void sendPasswordResetEmail(String toEmail, String employerName, String resetToken) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Password Reset Request - LifePill");
            
            String resetLink = passwordResetBaseUrl + "?token=" + resetToken;
            String htmlContent = emailTemplateService.generatePasswordResetEmail(employerName, resetLink);
            
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
            log.info("Password reset email sent to: {} with reset link: {}", toEmail, resetLink);
            
        } catch (MessagingException e) {
            log.error("Failed to send password reset email to {}: {}", toEmail, e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
