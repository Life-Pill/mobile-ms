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

    private EmployerDTO mapToDTO(Employer employer) {
        EmployerDTO dto = modelMapper.map(employer, EmployerDTO.class);
        dto.setActiveStatus(employer.isActiveStatus());
        return dto;
    }
}
