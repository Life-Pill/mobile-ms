package com.lifepill.inventoryservice.service.impl;

import com.lifepill.inventoryservice.dto.ItemCategoryDTO;
import com.lifepill.inventoryservice.entity.ItemCategory;
import com.lifepill.inventoryservice.exception.ResourceAlreadyExistsException;
import com.lifepill.inventoryservice.exception.ResourceNotFoundException;
import com.lifepill.inventoryservice.repository.ItemCategoryRepository;
import com.lifepill.inventoryservice.service.ItemCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of ItemCategoryService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ItemCategoryServiceImpl implements ItemCategoryService {
    
    private final ItemCategoryRepository categoryRepository;
    
    @Override
    public ItemCategoryDTO createCategory(ItemCategoryDTO categoryDTO) {
        log.info("Creating new category: {}", categoryDTO.getCategoryName());
        
        if (categoryRepository.existsByCategoryName(categoryDTO.getCategoryName())) {
            throw new ResourceAlreadyExistsException("Category", "name", categoryDTO.getCategoryName());
        }
        
        ItemCategory category = ItemCategory.builder()
                .categoryName(categoryDTO.getCategoryName())
                .categoryDescription(categoryDTO.getCategoryDescription())
                .categoryImage(categoryDTO.getCategoryImage())
                .build();
        
        ItemCategory savedCategory = categoryRepository.save(category);
        log.info("Category created successfully with ID: {}", savedCategory.getCategoryId());
        
        return mapToDTO(savedCategory);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ItemCategoryDTO getCategoryById(Long categoryId) {
        log.info("Fetching category with ID: {}", categoryId);
        ItemCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        return mapToDTO(category);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ItemCategoryDTO> getAllCategories() {
        log.info("Fetching all categories");
        return categoryRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public ItemCategoryDTO updateCategory(Long categoryId, ItemCategoryDTO categoryDTO) {
        log.info("Updating category with ID: {}", categoryId);
        
        ItemCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        
        if (categoryDTO.getCategoryName() != null) {
            category.setCategoryName(categoryDTO.getCategoryName());
        }
        if (categoryDTO.getCategoryDescription() != null) {
            category.setCategoryDescription(categoryDTO.getCategoryDescription());
        }
        if (categoryDTO.getCategoryImage() != null) {
            category.setCategoryImage(categoryDTO.getCategoryImage());
        }
        
        ItemCategory updatedCategory = categoryRepository.save(category);
        log.info("Category updated successfully with ID: {}", updatedCategory.getCategoryId());
        
        return mapToDTO(updatedCategory);
    }
    
    @Override
    public void deleteCategory(Long categoryId) {
        log.info("Deleting category with ID: {}", categoryId);
        
        ItemCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        
        categoryRepository.delete(category);
        log.info("Category deleted successfully with ID: {}", categoryId);
    }
    
    private ItemCategoryDTO mapToDTO(ItemCategory category) {
        return ItemCategoryDTO.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .categoryDescription(category.getCategoryDescription())
                .categoryImage(category.getCategoryImage())
                .build();
    }
}
