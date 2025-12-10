package com.lifepill.prescription.repository;

import com.lifepill.prescription.entity.PrescriptionResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PrescriptionResponseRepository extends JpaRepository<PrescriptionResponse, UUID> {
    
    Optional<PrescriptionResponse> findByPrescriptionIdAndBranchId(UUID prescriptionId, Long branchId);
    
    List<PrescriptionResponse> findByPrescriptionId(UUID prescriptionId);
    
    List<PrescriptionResponse> findByBranchId(Long branchId);
    
    @Query("SELECT pr FROM PrescriptionResponse pr LEFT JOIN FETCH pr.medicineAvailabilities WHERE pr.prescription.id = :prescriptionId")
    List<PrescriptionResponse> findByPrescriptionIdWithMedicines(UUID prescriptionId);
    
    boolean existsByPrescriptionIdAndBranchId(UUID prescriptionId, Long branchId);
}

