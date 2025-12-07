package com.lifepill.identityservice.service.impl;

import com.lifepill.identityservice.service.EmailTemplateService;
import org.springframework.stereotype.Service;

/**
 * Implementation of EmailTemplateService.
 * Provides modern, professional HTML email templates.
 */
@Service
public class EmailTemplateServiceImpl implements EmailTemplateService {
    
    @Override
    public String generatePasswordResetEmail(String employerName, String resetLink) {
        return buildModernEmailTemplate(
                "Password Reset Request",
                employerName,
                "We received a request to reset your password for your LifePill employer account.",
                resetLink,
                "Reset Password",
                "This link expires in 1 hour",
                "If you didn't request this, you can safely ignore this email."
        );
    }
    
    /**
     * Builds a modern, responsive email template.
     */
    private String buildModernEmailTemplate(
            String title,
            String recipientName,
            String message,
            String actionLink,
            String actionButtonText,
            String warningText,
            String footerNote) {
        
        return "<!DOCTYPE html>" +
                "<html lang=\"en\">" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "<title>" + title + "</title>" +
                "<style>" +
                "* { margin: 0; padding: 0; box-sizing: border-box; }" +
                "body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Helvetica', 'Arial', sans-serif; background-color: #f4f7fa; padding: 20px; }" +
                ".email-container { max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 16px; overflow: hidden; box-shadow: 0 4px 24px rgba(0, 0, 0, 0.08); }" +
                ".header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); padding: 40px 30px; text-align: center; }" +
                ".header h1 { color: #ffffff; font-size: 28px; font-weight: 700; margin: 0; }" +
                ".content { padding: 40px 30px; }" +
                ".greeting { font-size: 18px; color: #1a202c; margin-bottom: 20px; font-weight: 600; }" +
                ".message { font-size: 16px; color: #4a5568; line-height: 1.6; margin-bottom: 30px; }" +
                ".button-container { text-align: center; margin: 32px 0; }" +
                ".action-button { display: inline-block; padding: 16px 40px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: #ffffff; text-decoration: none; border-radius: 8px; font-weight: 600; font-size: 16px; transition: transform 0.2s, box-shadow 0.2s; }" +
                ".action-button:hover { transform: translateY(-2px); box-shadow: 0 8px 16px rgba(102, 126, 234, 0.3); }" +
                ".warning-box { background-color: #fef3c7; border-left: 4px solid: #f59e0b; padding: 16px; border-radius: 8px; margin: 24px 0; }" +
                ".warning-text { color: #92400e; font-size: 14px; font-weight: 600; margin: 0; }" +
                ".link-fallback { margin-top: 24px; padding: 16px; background-color: #f7fafc; border-radius: 8px; }" +
                ".link-fallback p { font-size: 14px; color: #4a5568; margin-bottom: 8px; }" +
                ".reset-link { word-break: break-all; color: #667eea; font-size: 13px; }" +
                ".footer-note { margin-top: 24px; padding-top: 24px; border-top: 1px solid #e2e8f0; color: #718096; font-size: 14px; line-height: 1.5; }" +
                ".footer { background-color: #f7fafc; padding: 24px 30px; text-align: center; }" +
                ".footer p { color: #718096; font-size: 13px; margin: 4px 0; }" +
                ".logo { width: 48px; height: 48px; background-color: rgba(255, 255, 255, 0.2); border-radius: 12px; display: inline-flex; align-items: center; justify-content: center; margin-bottom: 12px; font-size: 24px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class=\"email-container\">" +
                "<div class=\"header\">" +
                "<div class=\"logo\"></div>" +
                "<h1>LifePill</h1>" +
                "</div>" +
                "<div class=\"content\">" +
                "<p class=\"greeting\">Hello " + recipientName + ",</p>" +
                "<p class=\"message\">" + message + "</p>" +
                "<div class=\"button-container\">" +
                "<a href=\"" + actionLink + "\" class=\"action-button\">" + actionButtonText + "</a>" +
                "</div>" +
                "<div class=\"warning-box\">" +
                "<p class=\"warning-text\">⏰ " + warningText + "</p>" +
                "</div>" +
                "<div class=\"link-fallback\">" +
                "<p>If the button doesn't work, copy and paste this link into your browser:</p>" +
                "<p class=\"reset-link\">" + actionLink + "</p>" +
                "</div>" +
                "<div class=\"footer-note\">" +
                "<p>" + footerNote + "</p>" +
                "</div>" +
                "</div>" +
                "<div class=\"footer\">" +
                "<p>This is an automated message from LifePill</p>" +
                "<p>Please do not reply to this email</p>" +
                "<p style=\"margin-top: 12px;\">&copy; 2024 LifePill. All rights reserved.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}
