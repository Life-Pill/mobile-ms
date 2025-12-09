package com.lifepill.prescription.repository;

import com.lifepill.prescription.entity.Prescription;
import com.lifepill.prescription.entity.Prescription.PrescriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, UUID> {
    
    List<Prescription> findByUserIdOrderByUploadTimestampDesc(UUID userId);
    
    List<Prescription> findByStatusOrderByUploadTimestampDesc(PrescriptionStatus status);
    
    List<Prescription> findByUploadTimestampAfter(LocalDateTime timestamp);
    
    @Query("SELECT p FROM Prescription p LEFT JOIN FETCH p.responses WHERE p.id = :id")
    Prescription findByIdWithResponses(UUID id);
    
    long countByUserId(UUID userId);
}
