package com.lifepill.prescription.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionResponse {
    
    private UUID id;
    private UUID userId;
    private String imageUrl;
    private String notes;
    private String status;
    private LocalDateTime uploadTimestamp;
    private List<BranchResponseDTO> responses;
    private Integer totalResponses;
}
