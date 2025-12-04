package com.lifepill.inventoryservice.service;

import com.lifepill.inventoryservice.dto.ItemCategoryDTO;

import java.util.List;

/**
 * Service interface for ItemCategory operations
 */
public interface ItemCategoryService {
    
    ItemCategoryDTO createCategory(ItemCategoryDTO categoryDTO);
    
    ItemCategoryDTO getCategoryById(Long categoryId);
    
    List<ItemCategoryDTO> getAllCategories();
    
    ItemCategoryDTO updateCategory(Long categoryId, ItemCategoryDTO categoryDTO);
    
    void deleteCategory(Long categoryId);
}
