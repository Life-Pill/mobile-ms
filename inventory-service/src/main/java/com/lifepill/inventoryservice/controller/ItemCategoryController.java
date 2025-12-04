package com.lifepill.inventoryservice.controller;

import com.lifepill.inventoryservice.dto.ApiResponse;
import com.lifepill.inventoryservice.dto.ItemCategoryDTO;
import com.lifepill.inventoryservice.service.ItemCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for ItemCategory operations
 */
@RestController
@RequestMapping("/lifepill/v1/category")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Category Management", description = "APIs for managing item categories")
@CrossOrigin(origins = "*")
public class ItemCategoryController {
    
    private final ItemCategoryService categoryService;
    
    @PostMapping("/save")
    @Operation(summary = "Create category", description = "Create a new item category")
    public ResponseEntity<ApiResponse<ItemCategoryDTO>> createCategory(@RequestBody ItemCategoryDTO categoryDTO) {
        log.info("Creating new category: {}", categoryDTO.getCategoryName());
        ItemCategoryDTO createdCategory = categoryService.createCategory(categoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(createdCategory));
    }
    
    @GetMapping("/get-all")
    @Operation(summary = "Get all categories", description = "Retrieve all item categories")
    public ResponseEntity<ApiResponse<List<ItemCategoryDTO>>> getAllCategories() {
        log.info("Fetching all categories");
        List<ItemCategoryDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(ApiResponse.success(categories));
    }
    
    @GetMapping("/get-by-id/{categoryId}")
    @Operation(summary = "Get category by ID", description = "Retrieve a category by its ID")
    public ResponseEntity<ApiResponse<ItemCategoryDTO>> getCategoryById(@PathVariable Long categoryId) {
        log.info("Fetching category with ID: {}", categoryId);
        ItemCategoryDTO category = categoryService.getCategoryById(categoryId);
        return ResponseEntity.ok(ApiResponse.success(category));
    }
    
    @PutMapping("/update/{categoryId}")
    @Operation(summary = "Update category", description = "Update an existing category")
    public ResponseEntity<ApiResponse<ItemCategoryDTO>> updateCategory(
            @PathVariable Long categoryId,
            @RequestBody ItemCategoryDTO categoryDTO) {
        log.info("Updating category with ID: {}", categoryId);
        ItemCategoryDTO updatedCategory = categoryService.updateCategory(categoryId, categoryDTO);
        return ResponseEntity.ok(ApiResponse.success("Category updated successfully", updatedCategory));
    }
    
    @DeleteMapping("/delete/{categoryId}")
    @Operation(summary = "Delete category", description = "Delete a category by its ID")
    public ResponseEntity<ApiResponse<String>> deleteCategory(@PathVariable Long categoryId) {
        log.info("Deleting category with ID: {}", categoryId);
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success("Category deleted successfully", null));
    }
}
