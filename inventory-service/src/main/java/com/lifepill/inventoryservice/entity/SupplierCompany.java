package com.lifepill.inventoryservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * SupplierCompany entity
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "supplier_company")
public class SupplierCompany {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Long companyId;
    
    @Column(name = "company_name", length = 100)
    private String companyName;
    
    @Column(name = "company_address", length = 255)
    private String companyAddress;
    
    @Column(name = "company_contact", length = 20)
    private String companyContact;
    
    @Column(name = "company_email", length = 100)
    private String companyEmail;
    
    @Column(name = "company_description", length = 500)
    private String companyDescription;
    
    @Column(name = "company_status", length = 50)
    private String companyStatus;
    
    @Column(name = "company_rating", length = 20)
    private String companyRating;
    
    @Column(name = "company_bank", length = 100)
    private String companyBank;
    
    @Column(name = "company_account_number", length = 50)
    private String companyAccountNumber;
    
    @Column(name = "company_image", length = 500)
    private String companyImage;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "supplierCompany", fetch = FetchType.LAZY)
    private Set<Supplier> suppliers = new HashSet<>();
}
