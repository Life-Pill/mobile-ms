package com.lifepill.inventoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Paginated response DTO for items
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedResponseItemDTO {
    private List<ItemDTO> items;
    private int totalPages;
    private long totalElements;
    private int currentPage;
    private int pageSize;
    private boolean hasNext;
    private boolean hasPrevious;
}
