package com.lifepill.inventoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ItemCategory DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemCategoryDTO {
    private Long categoryId;
    private String categoryName;
    private String categoryDescription;
    private String categoryImage;
}
