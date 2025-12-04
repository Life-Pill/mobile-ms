package com.lifepill.inventoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SupplierCompany DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierCompanyDTO {
    private Long companyId;
    private String companyName;
    private String companyAddress;
    private String companyContact;
    private String companyEmail;
    private String companyDescription;
    private String companyStatus;
    private String companyRating;
    private String companyBank;
    private String companyAccountNumber;
    private String companyImage;
}
