package com.lifepill.branchservice.service.impl;

import com.lifepill.branchservice.dto.BranchDTO;
import com.lifepill.branchservice.dto.BranchRequestDTO;
import com.lifepill.branchservice.dto.BranchUpdateDTO;
import com.lifepill.branchservice.entity.Branch;
import com.lifepill.branchservice.exception.ResourceAlreadyExistsException;
import com.lifepill.branchservice.exception.ResourceNotFoundException;
import com.lifepill.branchservice.repository.BranchRepository;
import com.lifepill.branchservice.service.BranchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of BranchService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BranchServiceImpl implements BranchService {
    
    private final BranchRepository branchRepository;
    
    @Override
    public BranchDTO createBranch(BranchRequestDTO requestDTO) {
        log.info("Creating new branch: {}", requestDTO.getBranchName());
        
        if (branchRepository.existsByBranchEmail(requestDTO.getBranchEmail())) {
            throw new ResourceAlreadyExistsException("Branch", "email", requestDTO.getBranchEmail());
        }
        
        Branch branch = Branch.builder()
                .branchName(requestDTO.getBranchName())
                .branchAddress(requestDTO.getBranchAddress())
                .branchContact(requestDTO.getBranchContact())
                .branchFax(requestDTO.getBranchFax())
                .branchEmail(requestDTO.getBranchEmail())
                .branchDescription(requestDTO.getBranchDescription())
                .branchLocation(requestDTO.getBranchLocation())
                .branchCreatedBy(requestDTO.getBranchCreatedBy())
                .branchCreatedOn(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .branchLatitude(requestDTO.getBranchLatitude())
                .branchLongitude(requestDTO.getBranchLongitude())
                .branchStatus(true)
                .build();
        
        Branch savedBranch = branchRepository.save(branch);
        log.info("Branch created successfully with ID: {}", savedBranch.getBranchId());
        
        return mapToDTO(savedBranch);
    }
    
    @Override
    @Transactional(readOnly = true)
    public BranchDTO getBranchById(Long branchId) {
        log.info("Fetching branch with ID: {}", branchId);
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new ResourceNotFoundException("Branch", "id", branchId));
        return mapToDTO(branch);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BranchDTO> getAllBranches() {
        log.info("Fetching all branches");
        return branchRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BranchDTO> getActiveBranches() {
        log.info("Fetching active branches");
        return branchRepository.findByBranchStatus(true).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public BranchDTO updateBranch(Long branchId, BranchUpdateDTO updateDTO) {
        log.info("Updating branch with ID: {}", branchId);
        
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new ResourceNotFoundException("Branch", "id", branchId));
        
        if (updateDTO.getBranchName() != null) {
            branch.setBranchName(updateDTO.getBranchName());
        }
        if (updateDTO.getBranchAddress() != null) {
            branch.setBranchAddress(updateDTO.getBranchAddress());
        }
        if (updateDTO.getBranchContact() != null) {
            branch.setBranchContact(updateDTO.getBranchContact());
        }
        if (updateDTO.getBranchFax() != null) {
            branch.setBranchFax(updateDTO.getBranchFax());
        }
        if (updateDTO.getBranchEmail() != null) {
            branch.setBranchEmail(updateDTO.getBranchEmail());
        }
        if (updateDTO.getBranchDescription() != null) {
            branch.setBranchDescription(updateDTO.getBranchDescription());
        }
        if (updateDTO.getBranchLocation() != null) {
            branch.setBranchLocation(updateDTO.getBranchLocation());
        }
        if (updateDTO.getBranchLatitude() != null) {
            branch.setBranchLatitude(updateDTO.getBranchLatitude());
        }
        if (updateDTO.getBranchLongitude() != null) {
            branch.setBranchLongitude(updateDTO.getBranchLongitude());
        }
        
        Branch updatedBranch = branchRepository.save(branch);
        log.info("Branch updated successfully with ID: {}", updatedBranch.getBranchId());
        
        return mapToDTO(updatedBranch);
    }
    
    @Override
    public void deleteBranch(Long branchId) {
        log.info("Deleting branch with ID: {}", branchId);
        
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new ResourceNotFoundException("Branch", "id", branchId));
        
        branchRepository.delete(branch);
        log.info("Branch deleted successfully with ID: {}", branchId);
    }
    
    @Override
    public BranchDTO updateBranchStatus(Long branchId, boolean status) {
        log.info("Updating branch status for ID: {} to {}", branchId, status);
        
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new ResourceNotFoundException("Branch", "id", branchId));
        
        branch.setBranchStatus(status);
        Branch updatedBranch = branchRepository.save(branch);
        
        return mapToDTO(updatedBranch);
    }
    
    @Override
    public BranchDTO updateBranchImage(Long branchId, MultipartFile image) {
        log.info("Updating branch image for ID: {}", branchId);
        
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new ResourceNotFoundException("Branch", "id", branchId));
        
        try {
            branch.setBranchImage(image.getBytes());
            branch.setBranchImageUrl(image.getOriginalFilename());
        } catch (IOException e) {
            throw new RuntimeException("Failed to process image file", e);
        }
        
        Branch updatedBranch = branchRepository.save(branch);
        return mapToDTO(updatedBranch);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BranchDTO> searchBranchesByName(String name) {
        log.info("Searching branches by name: {}", name);
        return branchRepository.findByBranchNameContainingIgnoreCase(name).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BranchDTO> searchBranchesByLocation(String location) {
        log.info("Searching branches by location: {}", location);
        return branchRepository.findByBranchLocationContainingIgnoreCase(location).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public BranchDTO getBranchWithEmployers(Long branchId) {
        log.info("Fetching branch for ID: {} (employers now managed by Identity Service)", branchId);
        
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new ResourceNotFoundException("Branch", "id", branchId));
        
        // Note: Employers are now managed by Identity Service
        // Use IdentityServiceClient to fetch employers if needed
        return mapToDTO(branch);
    }
    
    private BranchDTO mapToDTO(Branch branch) {
        return BranchDTO.builder()
                .branchId(branch.getBranchId())
                .branchName(branch.getBranchName())
                .branchAddress(branch.getBranchAddress())
                .branchContact(branch.getBranchContact())
                .branchFax(branch.getBranchFax())
                .branchEmail(branch.getBranchEmail())
                .branchDescription(branch.getBranchDescription())
                .branchImage(branch.getBranchImageUrl())
                .branchImageData(branch.getBranchImage())
                .branchStatus(branch.isBranchStatus())
                .branchLocation(branch.getBranchLocation())
                .branchCreatedOn(branch.getBranchCreatedOn())
                .branchCreatedBy(branch.getBranchCreatedBy())
                .branchLatitude(branch.getBranchLatitude())
                .branchLongitude(branch.getBranchLongitude())
                .build();
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean branchExists(Long branchId) {
        log.info("Checking if branch exists with ID: {}", branchId);
        return branchRepository.existsById(branchId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countBranches() {
        log.info("Counting all branches");
        return branchRepository.count();
    }
}
