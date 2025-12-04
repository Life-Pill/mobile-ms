package com.lifepill.inventoryservice.service.impl;

import com.lifepill.inventoryservice.dto.ItemDTO;
import com.lifepill.inventoryservice.dto.ItemRequestDTO;
import com.lifepill.inventoryservice.dto.PaginatedResponseItemDTO;
import com.lifepill.inventoryservice.entity.Item;
import com.lifepill.inventoryservice.entity.ItemCategory;
import com.lifepill.inventoryservice.entity.Supplier;
import com.lifepill.inventoryservice.exception.ResourceNotFoundException;
import com.lifepill.inventoryservice.repository.ItemCategoryRepository;
import com.lifepill.inventoryservice.repository.ItemRepository;
import com.lifepill.inventoryservice.repository.SupplierRepository;
import com.lifepill.inventoryservice.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of ItemService
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ItemServiceImpl implements ItemService {
    
    private final ItemRepository itemRepository;
    private final ItemCategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;
    
    @Override
    public ItemDTO createItem(ItemRequestDTO requestDTO) {
        log.info("Creating new item: {}", requestDTO.getItemName());
        
        ItemCategory category = categoryRepository.findById(requestDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", requestDTO.getCategoryId()));
        
        Supplier supplier = supplierRepository.findById(requestDTO.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", requestDTO.getSupplierId()));
        
        Item item = mapToEntity(requestDTO, category, supplier);
        Item savedItem = itemRepository.save(item);
        
        log.info("Item created successfully with ID: {}", savedItem.getItemId());
        return mapToDTO(savedItem);
    }
    
    @Override
    public ItemDTO createItemWithImage(MultipartFile file, ItemRequestDTO requestDTO) {
        ItemDTO createdItem = createItem(requestDTO);
        if (file != null && !file.isEmpty()) {
            updateItemImage(createdItem.getItemId(), file);
            return getItemById(createdItem.getItemId());
        }
        return createdItem;
    }
    
    @Override
    @Transactional(readOnly = true)
    public ItemDTO getItemById(Long itemId) {
        log.info("Fetching item with ID: {}", itemId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item", "id", itemId));
        return mapToDTO(item);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ItemDTO> getAllItems() {
        log.info("Fetching all items");
        return itemRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ItemDTO> getItemsByName(String itemName) {
        log.info("Searching items by name: {}", itemName);
        return itemRepository.findByItemNameContainingIgnoreCase(itemName).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ItemDTO> getItemsByBarCode(String barCode) {
        log.info("Searching items by barcode: {}", barCode);
        return itemRepository.findByItemBarCode(barCode).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ItemDTO> getItemsByStock(boolean stock) {
        log.info("Fetching items by stock status: {}", stock);
        return itemRepository.findByStock(stock).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public PaginatedResponseItemDTO getItemsByStockPaginated(boolean stock, int page, int size) {
        log.info("Fetching paginated items by stock status: {}, page: {}, size: {}", stock, page, size);
        Page<Item> itemPage = itemRepository.findByStock(stock, PageRequest.of(page, size));
        
        List<ItemDTO> items = itemPage.getContent().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        
        return PaginatedResponseItemDTO.builder()
                .items(items)
                .totalPages(itemPage.getTotalPages())
                .totalElements(itemPage.getTotalElements())
                .currentPage(page)
                .pageSize(size)
                .hasNext(itemPage.hasNext())
                .hasPrevious(itemPage.hasPrevious())
                .build();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ItemDTO> getItemsByBranchId(Long branchId) {
        log.info("Fetching items for branch ID: {}", branchId);
        return itemRepository.findByBranchId(branchId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ItemDTO> getItemsByCategoryId(Long categoryId) {
        log.info("Fetching items for category ID: {}", categoryId);
        return itemRepository.findByItemCategoryCategoryId(categoryId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ItemDTO> getItemsBySupplierId(Long supplierId) {
        log.info("Fetching items for supplier ID: {}", supplierId);
        return itemRepository.findBySupplierSupplierId(supplierId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ItemDTO> getExpiredItems() {
        log.info("Fetching expired items");
        return itemRepository.findExpiredItems(LocalDate.now()).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ItemDTO> getItemsExpiringInDays(int days) {
        log.info("Fetching items expiring in {} days", days);
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(days);
        return itemRepository.findItemsExpiringBetween(startDate, endDate).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ItemDTO> getLowStockItems(Double threshold) {
        log.info("Fetching low stock items with threshold: {}", threshold);
        return itemRepository.findLowStockItems(threshold).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public ItemDTO updateItem(Long itemId, ItemRequestDTO requestDTO) {
        log.info("Updating item with ID: {}", itemId);
        
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item", "id", itemId));
        
        if (requestDTO.getCategoryId() != null) {
            ItemCategory category = categoryRepository.findById(requestDTO.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", requestDTO.getCategoryId()));
            item.setItemCategory(category);
        }
        
        if (requestDTO.getSupplierId() != null) {
            Supplier supplier = supplierRepository.findById(requestDTO.getSupplierId())
                    .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", requestDTO.getSupplierId()));
            item.setSupplier(supplier);
        }
        
        updateItemFields(item, requestDTO);
        Item updatedItem = itemRepository.save(item);
        
        log.info("Item updated successfully with ID: {}", updatedItem.getItemId());
        return mapToDTO(updatedItem);
    }
    
    @Override
    public void updateItemImage(Long itemId, MultipartFile image) {
        log.info("Updating image for item ID: {}", itemId);
        
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item", "id", itemId));
        
        // For now, store filename. In production, upload to S3/cloud storage
        item.setItemImage(image.getOriginalFilename());
        itemRepository.save(item);
    }
    
    @Override
    public void deleteItem(Long itemId) {
        log.info("Deleting item with ID: {}", itemId);
        
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item", "id", itemId));
        
        itemRepository.delete(item);
        log.info("Item deleted successfully with ID: {}", itemId);
    }
    
    @Override
    public void updateStock(Long itemId, Double quantity) {
        log.info("Updating stock for item ID: {} to quantity: {}", itemId, quantity);
        
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item", "id", itemId));
        
        item.setItemQuantity(quantity);
        item.setStock(quantity > 0);
        itemRepository.save(item);
    }
    
    private Item mapToEntity(ItemRequestDTO dto, ItemCategory category, Supplier supplier) {
        return Item.builder()
                .itemName(dto.getItemName())
                .sellingPrice(dto.getSellingPrice())
                .itemBarCode(dto.getItemBarCode())
                .supplyDate(dto.getSupplyDate())
                .supplierPrice(dto.getSupplierPrice())
                .isFreeIssued(dto.isFreeIssued())
                .isDiscounted(dto.isDiscounted())
                .itemManufacture(dto.getItemManufacture())
                .itemQuantity(dto.getItemQuantity())
                .stock(dto.isStock())
                .measuringUnitType(dto.getMeasuringUnitType())
                .manufactureDate(dto.getManufactureDate())
                .expireDate(dto.getExpireDate())
                .purchaseDate(dto.getPurchaseDate())
                .warrantyPeriod(dto.getWarrantyPeriod())
                .rackNumber(dto.getRackNumber())
                .discountedPrice(dto.getDiscountedPrice())
                .discountedPercentage(dto.getDiscountedPercentage())
                .warehouseName(dto.getWarehouseName())
                .isSpecialCondition(dto.isSpecialCondition())
                .itemDescription(dto.getItemDescription())
                .branchId(dto.getBranchId())
                .itemCategory(category)
                .supplier(supplier)
                .build();
    }
    
    private void updateItemFields(Item item, ItemRequestDTO dto) {
        if (dto.getItemName() != null) item.setItemName(dto.getItemName());
        if (dto.getSellingPrice() != null) item.setSellingPrice(dto.getSellingPrice());
        if (dto.getItemBarCode() != null) item.setItemBarCode(dto.getItemBarCode());
        if (dto.getSupplyDate() != null) item.setSupplyDate(dto.getSupplyDate());
        if (dto.getSupplierPrice() != null) item.setSupplierPrice(dto.getSupplierPrice());
        item.setFreeIssued(dto.isFreeIssued());
        item.setDiscounted(dto.isDiscounted());
        if (dto.getItemManufacture() != null) item.setItemManufacture(dto.getItemManufacture());
        if (dto.getItemQuantity() != null) item.setItemQuantity(dto.getItemQuantity());
        item.setStock(dto.isStock());
        if (dto.getMeasuringUnitType() != null) item.setMeasuringUnitType(dto.getMeasuringUnitType());
        if (dto.getManufactureDate() != null) item.setManufactureDate(dto.getManufactureDate());
        if (dto.getExpireDate() != null) item.setExpireDate(dto.getExpireDate());
        if (dto.getPurchaseDate() != null) item.setPurchaseDate(dto.getPurchaseDate());
        if (dto.getWarrantyPeriod() != null) item.setWarrantyPeriod(dto.getWarrantyPeriod());
        if (dto.getRackNumber() != null) item.setRackNumber(dto.getRackNumber());
        if (dto.getDiscountedPrice() != null) item.setDiscountedPrice(dto.getDiscountedPrice());
        if (dto.getDiscountedPercentage() != null) item.setDiscountedPercentage(dto.getDiscountedPercentage());
        if (dto.getWarehouseName() != null) item.setWarehouseName(dto.getWarehouseName());
        item.setSpecialCondition(dto.isSpecialCondition());
        if (dto.getItemDescription() != null) item.setItemDescription(dto.getItemDescription());
        if (dto.getBranchId() != null) item.setBranchId(dto.getBranchId());
    }
    
    private ItemDTO mapToDTO(Item item) {
        return ItemDTO.builder()
                .itemId(item.getItemId())
                .itemName(item.getItemName())
                .sellingPrice(item.getSellingPrice())
                .itemBarCode(item.getItemBarCode())
                .supplyDate(item.getSupplyDate())
                .supplierPrice(item.getSupplierPrice())
                .isFreeIssued(item.isFreeIssued())
                .isDiscounted(item.isDiscounted())
                .itemManufacture(item.getItemManufacture())
                .itemQuantity(item.getItemQuantity())
                .stock(item.isStock())
                .measuringUnitType(item.getMeasuringUnitType())
                .manufactureDate(item.getManufactureDate())
                .expireDate(item.getExpireDate())
                .purchaseDate(item.getPurchaseDate())
                .warrantyPeriod(item.getWarrantyPeriod())
                .rackNumber(item.getRackNumber())
                .discountedPrice(item.getDiscountedPrice())
                .discountedPercentage(item.getDiscountedPercentage())
                .warehouseName(item.getWarehouseName())
                .isSpecialCondition(item.isSpecialCondition())
                .itemImage(item.getItemImage())
                .itemDescription(item.getItemDescription())
                .branchId(item.getBranchId())
                .categoryId(item.getItemCategory().getCategoryId())
                .categoryName(item.getItemCategory().getCategoryName())
                .supplierId(item.getSupplier().getSupplierId())
                .supplierName(item.getSupplier().getSupplierName())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .build();
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countItemsByBranch(Long branchId) {
        log.info("Counting items for branch ID: {}", branchId);
        return itemRepository.countByBranchId(branchId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ItemDTO> getLowStockItemsByBranch(Long branchId, Double threshold) {
        log.info("Fetching low stock items for branch ID: {} with threshold: {}", branchId, threshold);
        return itemRepository.findByBranchId(branchId).stream()
                .filter(item -> item.getItemQuantity() <= threshold)
                .map(this::mapToDTO)
                .collect(java.util.stream.Collectors.toList());
    }
}
