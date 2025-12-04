package com.lifepill.possystem.client;

import com.lifepill.possystem.client.dto.MicroserviceApiResponse;
import com.lifepill.possystem.client.dto.MicroserviceItemDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Fallback implementation for Inventory Service Client
 * Provides default responses when the Inventory Service is unavailable
 */
@Component
public class InventoryServiceClientFallback implements InventoryServiceClient {

    @Override
    public ResponseEntity<MicroserviceApiResponse<MicroserviceItemDTO>> getItemById(Long itemId) {
        MicroserviceApiResponse<MicroserviceItemDTO> response = new MicroserviceApiResponse<>();
        response.setCode(503);
        response.setMessage("Inventory Service unavailable");
        response.setData(null);
        response.setSuccess(false);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<MicroserviceApiResponse<List<MicroserviceItemDTO>>> getItemsByBranch(Long branchId) {
        MicroserviceApiResponse<List<MicroserviceItemDTO>> response = new MicroserviceApiResponse<>();
        response.setCode(503);
        response.setMessage("Inventory Service unavailable");
        response.setData(Collections.emptyList());
        response.setSuccess(false);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<MicroserviceApiResponse<MicroserviceItemDTO>> updateStock(Long itemId, Double quantityChange) {
        MicroserviceApiResponse<MicroserviceItemDTO> response = new MicroserviceApiResponse<>();
        response.setCode(503);
        response.setMessage("Inventory Service unavailable - stock update deferred");
        response.setData(null);
        response.setSuccess(false);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<MicroserviceApiResponse<Boolean>> checkStock(Long itemId, Double quantity) {
        MicroserviceApiResponse<Boolean> response = new MicroserviceApiResponse<>();
        response.setCode(503);
        response.setMessage("Inventory Service unavailable - assuming stock available");
        response.setData(true); // Assume available when service is down
        response.setSuccess(false);
        return ResponseEntity.ok(response);
    }
}
