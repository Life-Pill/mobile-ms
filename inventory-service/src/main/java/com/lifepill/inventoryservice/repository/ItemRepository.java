package com.lifepill.inventoryservice.repository;

import com.lifepill.inventoryservice.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Item entity
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    
    List<Item> findByItemNameContainingIgnoreCase(String itemName);
    
    List<Item> findByItemBarCode(String barCode);
    
    Optional<Item> findByItemBarCodeAndBranchId(String barCode, Long branchId);
    
    List<Item> findByStock(boolean stock);
    
    Page<Item> findByStock(boolean stock, Pageable pageable);
    
    List<Item> findByBranchId(Long branchId);
    
    List<Item> findByItemCategoryCategoryId(Long categoryId);
    
    List<Item> findBySupplierSupplierId(Long supplierId);
    
    @Query("SELECT i FROM Item i WHERE i.expireDate <= :date")
    List<Item> findExpiredItems(@Param("date") LocalDate date);
    
    @Query("SELECT i FROM Item i WHERE i.expireDate BETWEEN :startDate AND :endDate")
    List<Item> findItemsExpiringBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT i FROM Item i WHERE i.itemQuantity <= :quantity")
    List<Item> findLowStockItems(@Param("quantity") Double quantity);
    
    List<Item> findByBranchIdAndStock(Long branchId, boolean stock);
    
    @Query("SELECT i FROM Item i WHERE i.branchId = :branchId AND i.itemCategory.categoryId = :categoryId")
    List<Item> findByBranchIdAndCategoryId(@Param("branchId") Long branchId, @Param("categoryId") Long categoryId);
    
    boolean existsByItemBarCode(String barCode);
    
    @Query("SELECT COUNT(i) FROM Item i WHERE i.branchId = :branchId")
    long countByBranchId(@Param("branchId") Long branchId);
}
