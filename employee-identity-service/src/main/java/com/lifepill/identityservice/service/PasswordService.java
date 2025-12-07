package com.lifepill.identityservice.service;

import com.lifepill.identityservice.dto.request.ForgotPasswordRequestDTO;
import com.lifepill.identityservice.dto.request.ResetPasswordDTO;
import com.lifepill.identityservice.dto.request.UpdatePasswordDTO;
import com.lifepill.identityservice.dto.request.UpdatePinDTO;

/**
 * Service for password and PIN management operations.
 */
public interface PasswordService {
    
    /**
     * Updates employer PIN with role hierarchy validation.
     *
     * @param requesterId ID of the requester (from JWT)
     * @param dto PIN update request
     * @return Success message
     */
    String updatePin(Long requesterId, UpdatePinDTO dto);
    
    /**
     * Updates employer password with role hierarchy validation.
     *
     * @param requesterId ID of the requester (from JWT)
     * @param dto Password update request
     * @return Success message
     */
    String updatePassword(Long requesterId, UpdatePasswordDTO dto);
    
    /**
     * Updates employer PIN with role hierarchy validation (email-based).
     *
     * @param requesterEmail Email of the requester (from JWT)
     * @param dto PIN update request
     * @return Success message
     */
    String updatePinByEmail(String requesterEmail, UpdatePinDTO dto);
    
    /**
     * Updates employer password with role hierarchy validation (email-based).
     *
     * @param requesterEmail Email of the requester (from JWT)
     * @param dto Password update request
     * @return Success message
     */
    String updatePasswordByEmail(String requesterEmail, UpdatePasswordDTO dto);
    
    /**
     * Initiates forgot password flow.
     *
     * @param dto Forgot password request
     * @return Success message
     */
    String forgotPassword(ForgotPasswordRequestDTO dto);
    
    /**
     * Resets password using token.
     *
     * @param dto Reset password request
     * @return Success message
     */
    String resetPassword(ResetPasswordDTO dto);
}
