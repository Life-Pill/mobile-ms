package com.lifepill.inventoryservice.controller;

import com.lifepill.inventoryservice.dto.ApiResponse;
import com.lifepill.inventoryservice.dto.ItemDTO;
import com.lifepill.inventoryservice.dto.ItemRequestDTO;
import com.lifepill.inventoryservice.dto.PaginatedResponseItemDTO;
import com.lifepill.inventoryservice.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * REST Controller for Item operations
 */
@RestController
@RequestMapping("/lifepill/v1/item")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Item Management", description = "APIs for managing inventory items/medicines")
@CrossOrigin(origins = "*")
public class ItemController {
    
    private final ItemService itemService;
    
    @PostMapping("/save")
    @Operation(summary = "Create item", description = "Create a new inventory item")
    public ResponseEntity<ApiResponse<ItemDTO>> saveItem(@RequestBody ItemRequestDTO requestDTO) {
        log.info("Creating new item: {}", requestDTO.getItemName());
        ItemDTO createdItem = itemService.createItem(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(createdItem));
    }
    
    @PostMapping(value = "/save-item-with-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create item with image", description = "Create a new item with an image")
    public ResponseEntity<ApiResponse<ItemDTO>> saveItemWithImage(
            @RequestParam(value = "file") MultipartFile file,
            @ModelAttribute ItemRequestDTO requestDTO) {
        log.info("Creating new item with image: {}", requestDTO.getItemName());
        ItemDTO createdItem = itemService.createItemWithImage(file, requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(createdItem));
    }
    
    @GetMapping("/get-all-items")
    @Operation(summary = "Get all items", description = "Retrieve all inventory items")
    public ResponseEntity<ApiResponse<List<ItemDTO>>> getAllItems() {
        log.info("Fetching all items");
        List<ItemDTO> items = itemService.getAllItems();
        return ResponseEntity.ok(ApiResponse.success(items));
    }
    
    @GetMapping("/get-item-details-by-id/{id}")
    @Operation(summary = "Get item by ID", description = "Retrieve an item by its ID")
    public ResponseEntity<ApiResponse<ItemDTO>> getItemById(@PathVariable("id") Long itemId) {
        log.info("Fetching item with ID: {}", itemId);
        ItemDTO item = itemService.getItemById(itemId);
        return ResponseEntity.ok(ApiResponse.success(item));
    }
    
    @GetMapping("/get-by-name")
    @Operation(summary = "Get items by name", description = "Search items by name")
    public ResponseEntity<ApiResponse<List<ItemDTO>>> getItemsByName(@RequestParam String itemName) {
        log.info("Searching items by name: {}", itemName);
        List<ItemDTO> items = itemService.getItemsByName(itemName);
        return ResponseEntity.ok(ApiResponse.success(items));
    }
    
    @GetMapping("/get-by-barcode")
    @Operation(summary = "Get items by barcode", description = "Search items by barcode")
    public ResponseEntity<ApiResponse<List<ItemDTO>>> getItemsByBarCode(@RequestParam String barcode) {
        log.info("Searching items by barcode: {}", barcode);
        List<ItemDTO> items = itemService.getItemsByBarCode(barcode);
        return ResponseEntity.ok(ApiResponse.success(items));
    }
    
    @GetMapping("/get-all-item-by-status")
    @Operation(summary = "Get items by stock status", description = "Retrieve items by their stock status")
    public ResponseEntity<ApiResponse<List<ItemDTO>>> getItemsByStock(@RequestParam boolean activeStatus) {
        log.info("Fetching items by stock status: {}", activeStatus);
        List<ItemDTO> items = itemService.getItemsByStock(activeStatus);
        return ResponseEntity.ok(ApiResponse.success(items));
    }
    
    @GetMapping("/get-all-item-by-status-lazy-initialized")
    @Operation(summary = "Get items by stock status (paginated)", description = "Retrieve items by stock status with pagination")
    public ResponseEntity<ApiResponse<PaginatedResponseItemDTO>> getItemsByStockPaginated(
            @RequestParam boolean activeStatus,
            @RequestParam int page,
            @RequestParam int size) {
        log.info("Fetching paginated items by stock status: {}", activeStatus);
        PaginatedResponseItemDTO response = itemService.getItemsByStockPaginated(activeStatus, page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    @GetMapping("/branched/get-item/{branchId}")
    @Operation(summary = "Get items by branch", description = "Retrieve items for a specific branch")
    public ResponseEntity<ApiResponse<List<ItemDTO>>> getItemsByBranch(@PathVariable Long branchId) {
        log.info("Fetching items for branch ID: {}", branchId);
        List<ItemDTO> items = itemService.getItemsByBranchId(branchId);
        return ResponseEntity.ok(ApiResponse.success(items));
    }
    
    @GetMapping("/get-by-category/{categoryId}")
    @Operation(summary = "Get items by category", description = "Retrieve items for a specific category")
    public ResponseEntity<ApiResponse<List<ItemDTO>>> getItemsByCategory(@PathVariable Long categoryId) {
        log.info("Fetching items for category ID: {}", categoryId);
        List<ItemDTO> items = itemService.getItemsByCategoryId(categoryId);
        return ResponseEntity.ok(ApiResponse.success(items));
    }
    
    @GetMapping("/get-by-supplier/{supplierId}")
    @Operation(summary = "Get items by supplier", description = "Retrieve items for a specific supplier")
    public ResponseEntity<ApiResponse<List<ItemDTO>>> getItemsBySupplier(@PathVariable Long supplierId) {
        log.info("Fetching items for supplier ID: {}", supplierId);
        List<ItemDTO> items = itemService.getItemsBySupplierId(supplierId);
        return ResponseEntity.ok(ApiResponse.success(items));
    }
    
    @GetMapping("/get-expired-items")
    @Operation(summary = "Get expired items", description = "Retrieve all expired items")
    public ResponseEntity<ApiResponse<List<ItemDTO>>> getExpiredItems() {
        log.info("Fetching expired items");
        List<ItemDTO> items = itemService.getExpiredItems();
        return ResponseEntity.ok(ApiResponse.success(items));
    }
    
    @GetMapping("/get-items-expiring-soon")
    @Operation(summary = "Get items expiring soon", description = "Retrieve items expiring within specified days")
    public ResponseEntity<ApiResponse<List<ItemDTO>>> getItemsExpiringSoon(@RequestParam(defaultValue = "30") int days) {
        log.info("Fetching items expiring in {} days", days);
        List<ItemDTO> items = itemService.getItemsExpiringInDays(days);
        return ResponseEntity.ok(ApiResponse.success(items));
    }
    
    @GetMapping("/get-low-stock-items")
    @Operation(summary = "Get low stock items", description = "Retrieve items with quantity below threshold")
    public ResponseEntity<ApiResponse<List<ItemDTO>>> getLowStockItems(@RequestParam(defaultValue = "10") Double threshold) {
        log.info("Fetching low stock items with threshold: {}", threshold);
        List<ItemDTO> items = itemService.getLowStockItems(threshold);
        return ResponseEntity.ok(ApiResponse.success(items));
    }
    
    @PutMapping("/update")
    @Operation(summary = "Update item", description = "Update an existing item")
    public ResponseEntity<ApiResponse<ItemDTO>> updateItem(
            @RequestParam Long itemId,
            @RequestBody ItemRequestDTO requestDTO) {
        log.info("Updating item with ID: {}", itemId);
        ItemDTO updatedItem = itemService.updateItem(itemId, requestDTO);
        return ResponseEntity.ok(ApiResponse.success("Item updated successfully", updatedItem));
    }
    
    @PutMapping(value = "/update-item-image/{itemId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update item image", description = "Upload and update item image")
    public ResponseEntity<ApiResponse<String>> updateItemImage(
            @PathVariable Long itemId,
            @RequestPart("file") MultipartFile file) {
        log.info("Updating image for item ID: {}", itemId);
        itemService.updateItemImage(itemId, file);
        return ResponseEntity.ok(ApiResponse.success("Item image updated successfully", null));
    }
    
    @DeleteMapping("/delete-item/{id}")
    @Operation(summary = "Delete item", description = "Delete an item by its ID")
    public ResponseEntity<ApiResponse<String>> deleteItem(@PathVariable("id") Long itemId) {
        log.info("Deleting item with ID: {}", itemId);
        itemService.deleteItem(itemId);
        return ResponseEntity.ok(ApiResponse.success("Item deleted successfully", null));
    }
    
    @PutMapping("/update-stock/{itemId}")
    @Operation(summary = "Update item stock", description = "Update the stock quantity of an item")
    public ResponseEntity<ApiResponse<String>> updateStock(
            @PathVariable Long itemId,
            @RequestParam Double quantity) {
        log.info("Updating stock for item ID: {} to quantity: {}", itemId, quantity);
        itemService.updateStock(itemId, quantity);
        return ResponseEntity.ok(ApiResponse.success("Stock updated successfully", null));
    }
    
    @GetMapping("/by-branch/{branchId}")
    @Operation(summary = "Get items by branch (for Feign)", description = "Retrieve items for a specific branch - used by other services")
    public ResponseEntity<ApiResponse<List<ItemDTO>>> getItemsByBranchForFeign(@PathVariable Long branchId) {
        log.info("Feign: Fetching items for branch ID: {}", branchId);
        List<ItemDTO> items = itemService.getItemsByBranchId(branchId);
        return ResponseEntity.ok(ApiResponse.success(items));
    }
    
    @GetMapping("/count-by-branch")
    @Operation(summary = "Count items by branch", description = "Get count of items for a specific branch")
    public ResponseEntity<ApiResponse<Long>> countItemsByBranch(@RequestParam Long branchId) {
        log.info("Counting items for branch ID: {}", branchId);
        long count = itemService.countItemsByBranch(branchId);
        return ResponseEntity.ok(ApiResponse.success(count));
    }
    
    @GetMapping("/low-stock/{branchId}")
    @Operation(summary = "Get low stock items by branch", description = "Get low stock items for a specific branch")
    public ResponseEntity<ApiResponse<List<ItemDTO>>> getLowStockItemsByBranch(
            @PathVariable Long branchId,
            @RequestParam(defaultValue = "10") Double threshold) {
        log.info("Fetching low stock items for branch ID: {} with threshold: {}", branchId, threshold);
        List<ItemDTO> items = itemService.getLowStockItemsByBranch(branchId, threshold);
        return ResponseEntity.ok(ApiResponse.success(items));
    }
}
