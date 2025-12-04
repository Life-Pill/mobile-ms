package com.lifepill.inventoryservice.repository;

import com.lifepill.inventoryservice.entity.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for ItemCategory entity
 */
@Repository
public interface ItemCategoryRepository extends JpaRepository<ItemCategory, Long> {
    
    Optional<ItemCategory> findByCategoryName(String categoryName);
    
    boolean existsByCategoryName(String categoryName);
}
