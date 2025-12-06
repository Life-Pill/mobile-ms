package com.lifepill.identityservice.service.impl;

import com.lifepill.identityservice.client.BranchServiceClient;
import com.lifepill.identityservice.client.dto.MicroserviceApiResponse;
import com.lifepill.identityservice.dto.EmployerDTO;
import com.lifepill.identityservice.entity.Employer;
import com.lifepill.identityservice.entity.enums.Role;
import com.lifepill.identityservice.exception.NotFoundException;
import com.lifepill.identityservice.repository.EmployerRepository;
import com.lifepill.identityservice.service.EmployerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of EmployerService for employer management operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EmployerServiceImpl implements EmployerService {

    private final EmployerRepository employerRepository;
    private final BranchServiceClient branchServiceClient;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public EmployerDTO getEmployerById(Long employerId) {
        Employer employer = employerRepository.findById(employerId)
                .orElseThrow(() -> new NotFoundException("Employer not found with ID: " + employerId));
        return mapToDTO(employer);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployerDTO getEmployerByEmail(String email) {
        Employer employer = employerRepository.findByEmployerEmail(email)
                .orElseThrow(() -> new NotFoundException("Employer not found with email: " + email));
        return mapToDTO(employer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployerDTO> getAllEmployers() {
        return employerRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployerDTO> getEmployersByBranch(Long branchId) {
        return employerRepository.findByBranchId(branchId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployerDTO> getEmployersByRole(Role role) {
        return employerRepository.findByRole(role).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EmployerDTO updateEmployer(Long employerId, EmployerDTO employerDTO) {
        Employer employer = employerRepository.findById(employerId)
                .orElseThrow(() -> new NotFoundException("Employer not found with ID: " + employerId));

        // Update fields
        if (employerDTO.getEmployerFirstName() != null) {
            employer.setEmployerFirstName(employerDTO.getEmployerFirstName());
        }
        if (employerDTO.getEmployerLastName() != null) {
            employer.setEmployerLastName(employerDTO.getEmployerLastName());
        }
        if (employerDTO.getEmployerNicName() != null) {
            employer.setEmployerNicName(employerDTO.getEmployerNicName());
        }
        if (employerDTO.getEmployerPhone() != null) {
            employer.setEmployerPhone(employerDTO.getEmployerPhone());
        }
        if (employerDTO.getEmployerAddress() != null) {
            employer.setEmployerAddress(employerDTO.getEmployerAddress());
        }
        if (employerDTO.getEmployerSalary() > 0) {
            employer.setEmployerSalary(employerDTO.getEmployerSalary());
        }
        if (employerDTO.getGender() != null) {
            employer.setGender(employerDTO.getGender());
        }
        if (employerDTO.getDateOfBirth() != null) {
            employer.setDateOfBirth(employerDTO.getDateOfBirth());
        }
        if (employerDTO.getProfileImageUrl() != null) {
            employer.setProfileImageUrl(employerDTO.getProfileImageUrl());
        }
        if (employerDTO.getPin() > 0) {
            employer.setPin(employerDTO.getPin());
        }

        Employer savedEmployer = employerRepository.save(employer);
        log.info("Employer updated: {}", savedEmployer.getEmployerId());
        return mapToDTO(savedEmployer);
    }

    @Override
    public void deleteEmployer(Long employerId) {
        if (!employerRepository.existsById(employerId)) {
            throw new NotFoundException("Employer not found with ID: " + employerId);
        }
        employerRepository.deleteById(employerId);
        log.info("Employer deleted: {}", employerId);
    }

    @Override
    public EmployerDTO updateEmployerBranch(Long employerId, Long branchId) {
        Employer employer = employerRepository.findById(employerId)
                .orElseThrow(() -> new NotFoundException("Employer not found with ID: " + employerId));

        // Verify branch exists
        try {
            ResponseEntity<MicroserviceApiResponse<Boolean>> branchExists = 
                    branchServiceClient.branchExists(branchId);
            if (branchExists.getBody() == null || !Boolean.TRUE.equals(branchExists.getBody().getData())) {
                throw new NotFoundException("Branch not found with ID: " + branchId);
            }
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.warn("Error verifying branch existence, proceeding without verification: {}", e.getMessage());
        }

        employer.setBranchId(branchId);
        Employer savedEmployer = employerRepository.save(employer);
        log.info("Employer {} assigned to branch {}", employerId, branchId);
        return mapToDTO(savedEmployer);
    }

    @Override
    @Transactional(readOnly = true)
    public long countEmployersByBranch(Long branchId) {
        return employerRepository.countByBranchId(branchId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployerDTO> getActiveEmployersByBranch(Long branchId) {
        return employerRepository.findByBranchIdAndIsActiveStatus(branchId, true).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployerDTO> getManagersByBranch(Long branchId) {
        log.info("Getting managers for branch: {}", branchId);
        return employerRepository.findByBranchIdAndRole(branchId, Role.MANAGER).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployerDTO> getEmployersByBranchAndRole(Long branchId, Role role) {
        log.info("Getting employers for branch: {} with role: {}", branchId, role);
        return employerRepository.findByBranchIdAndRole(branchId, role).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EmployerDTO changeEmployerRole(Long employerId, Role newRole) {
        log.info("Changing role for employer {} to {}", employerId, newRole);
        
        Employer employer = employerRepository.findById(employerId)
                .orElseThrow(() -> new NotFoundException("Employer not found with ID: " + employerId));
        
        employer.setRole(newRole);
        Employer savedEmployer = employerRepository.save(employer);
        
        log.info("Successfully changed role for employer {} to {}", employerId, newRole);
        return mapToDTO(savedEmployer);
    }

    @Override
    public EmployerDTO createEmployer(Long branchId, com.lifepill.identityservice.dto.request.CreateEmployerRequestDTO requestDTO) {
        log.info("Creating employer for branch: {}", branchId);
        
        // Check if email already exists
        if (employerRepository.existsByEmployerEmail(requestDTO.getEmployerEmail())) {
            throw new RuntimeException("Employer with email " + requestDTO.getEmployerEmail() + " already exists");
        }
        
        // Create new employer entity
        Employer employer = new Employer();
        employer.setEmployerFirstName(requestDTO.getEmployerFirstName());
        employer.setEmployerLastName(requestDTO.getEmployerLastName());
        employer.setEmployerEmail(requestDTO.getEmployerEmail());
        employer.setEmployerNicName(requestDTO.getEmployerFirstName().toLowerCase());
        employer.setBranchId(branchId);
        employer.setActiveStatus(true);
        
        // Set default values for required fields
        employer.setEmployerSalary(50000.0); // Default salary
        employer.setEmployerPhone("0000000000"); // Default phone
        employer.setEmployerAddress("Not provided"); // Default address
        employer.setEmployerNic("000000000V"); // Default NIC
        employer.setGender(com.lifepill.identityservice.entity.enums.Gender.MALE); // Default gender
        
        // Set role
        if (requestDTO.getRole() != null) {
            employer.setRole(Role.valueOf(requestDTO.getRole().toUpperCase()));
        } else {
            employer.setRole(Role.OTHER);
        }
        
        // Set pin
        if (requestDTO.getPin() != null) {
            employer.setPin(requestDTO.getPin());
        } else {
            employer.setPin(1234); // Default pin
        }
        
        // Hash password if provided
        if (requestDTO.getEmployerPassword() != null) {
            org.springframework.security.crypto.password.PasswordEncoder passwordEncoder = 
                new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
            employer.setEmployerPassword(passwordEncoder.encode(requestDTO.getEmployerPassword()));
        }
        
        Employer savedEmployer = employerRepository.save(employer);
        log.info("Created employer with ID: {}", savedEmployer.getEmployerId());
        
        return mapToDTO(savedEmployer);
    }

    private EmployerDTO mapToDTO(Employer employer) {
        EmployerDTO dto = modelMapper.map(employer, EmployerDTO.class);
        dto.setActiveStatus(employer.isActiveStatus());
        return dto;
    }
}
