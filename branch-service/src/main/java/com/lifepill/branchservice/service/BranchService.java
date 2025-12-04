package com.lifepill.branchservice.service;

import com.lifepill.branchservice.dto.BranchDTO;
import com.lifepill.branchservice.dto.BranchRequestDTO;
import com.lifepill.branchservice.dto.BranchUpdateDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Service interface for Branch operations
 */
public interface BranchService {
    
    BranchDTO createBranch(BranchRequestDTO requestDTO);
    
    BranchDTO getBranchById(Long branchId);
    
    List<BranchDTO> getAllBranches();
    
    List<BranchDTO> getActiveBranches();
    
    BranchDTO updateBranch(Long branchId, BranchUpdateDTO updateDTO);
    
    void deleteBranch(Long branchId);
    
    BranchDTO updateBranchStatus(Long branchId, boolean status);
    
    BranchDTO updateBranchImage(Long branchId, MultipartFile image);
    
    List<BranchDTO> searchBranchesByName(String name);
    
    List<BranchDTO> searchBranchesByLocation(String location);
    
    BranchDTO getBranchWithEmployers(Long branchId);
    
    boolean branchExists(Long branchId);
    
    long countBranches();
}
