package com.lifepill.identityservice.service.impl;

import com.lifepill.identityservice.client.BranchServiceClient;
import com.lifepill.identityservice.client.dto.MicroserviceApiResponse;
import com.lifepill.identityservice.config.JwtService;
import com.lifepill.identityservice.dto.request.AuthenticationRequestDTO;
import com.lifepill.identityservice.dto.request.PinAuthenticationRequestDTO;
import com.lifepill.identityservice.dto.request.RegisterRequestDTO;
import com.lifepill.identityservice.dto.response.AuthenticationResponseDTO;
import com.lifepill.identityservice.dto.response.EmployerAuthDetailsResponseDTO;
import com.lifepill.identityservice.entity.Employer;
import com.lifepill.identityservice.exception.AuthenticationException;
import com.lifepill.identityservice.exception.EntityDuplicationException;
import com.lifepill.identityservice.exception.NotFoundException;
import com.lifepill.identityservice.repository.EmployerRepository;
import com.lifepill.identityservice.security.EmployerUserDetails;
import com.lifepill.identityservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of AuthService for handling authentication operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthServiceImpl implements AuthService {

    private final EmployerRepository employerRepository;
    private final JwtService jwtService;
    private final BranchServiceClient branchServiceClient;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponseDTO register(RegisterRequestDTO registerRequest) {
        // Check if employer already exists
        if (employerRepository.existsAllByEmployerEmail(registerRequest.getEmployerEmail())) {
            throw new EntityDuplicationException("Employer with email " + registerRequest.getEmployerEmail() + " already exists");
        }

        // Verify branch exists via Branch Service
        try {
            ResponseEntity<MicroserviceApiResponse<Boolean>> branchExists = 
                    branchServiceClient.branchExists(registerRequest.getBranchId());
            if (branchExists.getBody() == null || !Boolean.TRUE.equals(branchExists.getBody().getData())) {
                throw new NotFoundException("Branch not found with ID: " + registerRequest.getBranchId());
            }
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.warn("Error verifying branch existence, proceeding without verification: {}", e.getMessage());
            // Continue without branch verification if Branch Service is unavailable
        }

        // Create employer entity
        Employer employer = modelMapper.map(registerRequest, Employer.class);
        employer.setEmployerPassword(passwordEncoder.encode(registerRequest.getEmployerPassword()));
        employer.setBranchId(registerRequest.getBranchId());
        employer.setActiveStatus(false);

        // Save employer
        Employer savedEmployer = employerRepository.save(employer);
        log.info("Employer registered successfully: {}", savedEmployer.getEmployerEmail());

        // Generate JWT token
        String jwtToken = jwtService.generateToken(new EmployerUserDetails(savedEmployer));
        String refreshToken = jwtService.generateRefreshToken(new EmployerUserDetails(savedEmployer));

        return AuthenticationResponseDTO.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .message("Registration successful")
                .employerId(savedEmployer.getEmployerId())
                .employerEmail(savedEmployer.getEmployerEmail())
                .role(savedEmployer.getRole().name())
                .build();
    }

    @Override
    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request) {
        try {
            // Authenticate user using Spring Security's authenticationManager
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmployerEmail(),
                            request.getEmployerPassword()
                    )
            );

            // If authentication is successful, get the authenticated employer
            Employer authenticatedEmployer = employerRepository.findByEmployerEmail(request.getEmployerEmail())
                    .orElseThrow(() -> new AuthenticationException("User not found"));

            // Update active status
            authenticatedEmployer.setActiveStatus(true);
            employerRepository.save(authenticatedEmployer);

            // Create UserDetails and generate token
            UserDetails employerUserDetails = new EmployerUserDetails(authenticatedEmployer);
            String jwtToken = jwtService.generateToken(employerUserDetails);
            String refreshToken = jwtService.generateRefreshToken(employerUserDetails);

            log.info("User authenticated successfully: {}", request.getEmployerEmail());

            return AuthenticationResponseDTO.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .message("Authentication successful")
                    .employerId(authenticatedEmployer.getEmployerId())
                    .employerEmail(authenticatedEmployer.getEmployerEmail())
                    .role(authenticatedEmployer.getRole().name())
                    .build();

        } catch (org.springframework.security.core.AuthenticationException e) {
            log.error("Authentication failed for user: {}", request.getEmployerEmail());
            throw new AuthenticationException("Incorrect username or password", e);
        }
    }

    @Override
    public AuthenticationResponseDTO authenticateWithPin(PinAuthenticationRequestDTO request) {
        Employer employer = employerRepository.findByEmployerEmail(request.getEmployerEmail())
                .orElseThrow(() -> new AuthenticationException("User not found"));

        if (employer.getPin() != request.getPin()) {
            throw new AuthenticationException("Invalid PIN");
        }

        // Update active status
        employer.setActiveStatus(true);
        employerRepository.save(employer);

        // Generate token
        UserDetails employerUserDetails = new EmployerUserDetails(employer);
        String jwtToken = jwtService.generateToken(employerUserDetails);
        String refreshToken = jwtService.generateRefreshToken(employerUserDetails);

        log.info("User authenticated with PIN: {}", request.getEmployerEmail());

        return AuthenticationResponseDTO.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .message("PIN authentication successful")
                .employerId(employer.getEmployerId())
                .employerEmail(employer.getEmployerEmail())
                .role(employer.getRole().name())
                .build();
    }

    @Override
    public EmployerAuthDetailsResponseDTO getEmployerDetails(String email) {
        Employer employer = employerRepository.findByEmployerEmail(email)
                .orElseThrow(() -> new NotFoundException("Employer not found with email: " + email));

        return EmployerAuthDetailsResponseDTO.builder()
                .employerId(employer.getEmployerId())
                .employerNicName(employer.getEmployerNicName())
                .employerFirstName(employer.getEmployerFirstName())
                .employerLastName(employer.getEmployerLastName())
                .profileImageUrl(employer.getProfileImageUrl())
                .employerEmail(employer.getEmployerEmail())
                .employerPhone(employer.getEmployerPhone())
                .employerAddress(employer.getEmployerAddress())
                .employerSalary(employer.getEmployerSalary())
                .employerNic(employer.getEmployerNic())
                .activeStatus(employer.isActiveStatus())
                .pin(employer.getPin())
                .gender(employer.getGender())
                .dateOfBirth(employer.getDateOfBirth())
                .role(employer.getRole())
                .branchId(employer.getBranchId())
                .build();
    }

    @Override
    public AuthenticationResponseDTO generateAuthenticationResponse(String employerEmail) {
        Employer employer = employerRepository.findByEmployerEmail(employerEmail)
                .orElseThrow(() -> new AuthenticationException("User not found"));

        UserDetails employerUserDetails = new EmployerUserDetails(employer);
        String jwtToken = jwtService.generateToken(employerUserDetails);

        return AuthenticationResponseDTO.builder()
                .accessToken(jwtToken)
                .employerId(employer.getEmployerId())
                .employerEmail(employer.getEmployerEmail())
                .role(employer.getRole().name())
                .build();
    }

    @Override
    public void setActiveStatus(String email, boolean isActive) {
        Employer employer = employerRepository.findByEmployerEmail(email)
                .orElseThrow(() -> new NotFoundException("Employer not found with email: " + email));

        employer.setActiveStatus(isActive);
        employerRepository.save(employer);
        log.info("Updated active status for {}: {}", email, isActive);
    }

    @Override
    public EmployerAuthDetailsResponseDTO validateToken(String token) {
        try {
            String email = jwtService.extractUsername(token);
            Employer employer = employerRepository.findByEmployerEmail(email)
                    .orElseThrow(() -> new AuthenticationException("User not found"));

            UserDetails userDetails = new EmployerUserDetails(employer);
            if (!jwtService.isTokenValid(token, userDetails)) {
                throw new AuthenticationException("Invalid token");
            }

            return getEmployerDetails(email);
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
            throw new AuthenticationException("Invalid or expired token");
        }
    }
}
