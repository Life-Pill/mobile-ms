package com.lifepill.branchservice.repository;

import com.lifepill.branchservice.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Branch entity
 */
@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
    
    Optional<Branch> findByBranchEmail(String branchEmail);
    
    List<Branch> findByBranchStatus(boolean status);
    
    List<Branch> findByBranchNameContainingIgnoreCase(String branchName);
    
    boolean existsByBranchEmail(String branchEmail);
    
    List<Branch> findByBranchLocationContainingIgnoreCase(String location);
}
