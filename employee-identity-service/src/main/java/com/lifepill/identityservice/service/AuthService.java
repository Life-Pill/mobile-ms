package com.lifepill.identityservice.service;

import com.lifepill.identityservice.dto.request.AuthenticationRequestDTO;
import com.lifepill.identityservice.dto.request.PinAuthenticationRequestDTO;
import com.lifepill.identityservice.dto.request.RegisterRequestDTO;
import com.lifepill.identityservice.dto.response.AuthenticationResponseDTO;
import com.lifepill.identityservice.dto.response.EmployerAuthDetailsResponseDTO;

/**
 * Service interface for authentication operations.
 */
public interface AuthService {

    /**
     * Registers a new employer.
     *
     * @param registerRequest The registration request
     * @return Authentication response with token
     */
    AuthenticationResponseDTO register(RegisterRequestDTO registerRequest);

    /**
     * Authenticates an employer with email and password.
     *
     * @param request The authentication request
     * @return Authentication response with token
     */
    AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request);

    /**
     * Authenticates an employer with email and PIN.
     *
     * @param request The PIN authentication request
     * @return Authentication response with token
     */
    AuthenticationResponseDTO authenticateWithPin(PinAuthenticationRequestDTO request);

    /**
     * Gets employer details by email.
     *
     * @param email The employer's email
     * @return Employer authentication details
     */
    EmployerAuthDetailsResponseDTO getEmployerDetails(String email);

    /**
     * Generates authentication response for an existing employer.
     *
     * @param employerEmail The employer's email
     * @return Authentication response with new token
     */
    AuthenticationResponseDTO generateAuthenticationResponse(String employerEmail);

    /**
     * Sets the active status of an employer.
     *
     * @param email The employer's email
     * @param isActive The active status to set
     */
    void setActiveStatus(String email, boolean isActive);

    /**
     * Validates a JWT token and returns the associated employer details.
     *
     * @param token The JWT token to validate
     * @return Employer details if token is valid
     */
    EmployerAuthDetailsResponseDTO validateToken(String token);
}
