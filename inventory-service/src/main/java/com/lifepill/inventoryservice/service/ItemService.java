package com.lifepill.inventoryservice.service;

import com.lifepill.inventoryservice.dto.ItemDTO;
import com.lifepill.inventoryservice.dto.ItemRequestDTO;
import com.lifepill.inventoryservice.dto.PaginatedResponseItemDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Service interface for Item operations
 */
public interface ItemService {
    
    ItemDTO createItem(ItemRequestDTO requestDTO);
    
    ItemDTO createItemWithImage(MultipartFile file, ItemRequestDTO requestDTO);
    
    ItemDTO getItemById(Long itemId);
    
    List<ItemDTO> getAllItems();
    
    List<ItemDTO> getItemsByName(String itemName);
    
    List<ItemDTO> getItemsByBarCode(String barCode);
    
    List<ItemDTO> getItemsByStock(boolean stock);
    
    PaginatedResponseItemDTO getItemsByStockPaginated(boolean stock, int page, int size);
    
    List<ItemDTO> getItemsByBranchId(Long branchId);
    
    List<ItemDTO> getItemsByCategoryId(Long categoryId);
    
    List<ItemDTO> getItemsBySupplierId(Long supplierId);
    
    List<ItemDTO> getExpiredItems();
    
    List<ItemDTO> getItemsExpiringInDays(int days);
    
    List<ItemDTO> getLowStockItems(Double threshold);
    
    ItemDTO updateItem(Long itemId, ItemRequestDTO requestDTO);
    
    void updateItemImage(Long itemId, MultipartFile image);
    
    void deleteItem(Long itemId);
    
    void updateStock(Long itemId, Double quantity);
    
    long countItemsByBranch(Long branchId);
    
    List<ItemDTO> getLowStockItemsByBranch(Long branchId, Double threshold);
}
