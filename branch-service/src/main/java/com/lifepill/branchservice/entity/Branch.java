package com.lifepill.branchservice.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Branch entity representing pharmacy branches.
 * Employers are managed by Identity Service with branchId reference.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "branch")
@Builder
public class Branch {
    
    @Id
    @Column(name = "branch_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long branchId;
    
    @Column(name = "branch_name", length = 100, nullable = false)
    private String branchName;
    
    @Column(name = "branch_address", length = 255, nullable = false)
    private String branchAddress;
    
    @Column(name = "branch_contact", length = 20)
    private String branchContact;
    
    @Column(name = "branch_fax", length = 20)
    private String branchFax;
    
    @Column(name = "branch_email", length = 100)
    private String branchEmail;
    
    @Column(name = "branch_description", length = 500)
    private String branchDescription;
    
    @Lob
    @Column(name = "branch_image")
    private byte[] branchImage;
    
    @Column(name = "branch_image_url", length = 500)
    private String branchImageUrl;
    
    @Column(name = "branch_status", columnDefinition = "BOOLEAN default true")
    private boolean branchStatus;
    
    @Column(name = "branch_location", length = 255)
    private String branchLocation;
    
    @Column(name = "branch_created_on", length = 50)
    private String branchCreatedOn;
    
    @Column(name = "branch_created_by", length = 100)
    private String branchCreatedBy;
    
    @Column(name = "branch_latitude")
    private Double branchLatitude;
    
    @Column(name = "branch_longitude")
    private Double branchLongitude;
    
    @Column(name = "opening_hours", length = 100)
    private String openingHours;
    
    @Column(name = "closing_hours", length = 100)
    private String closingHours;
}
