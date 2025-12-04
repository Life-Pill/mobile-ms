package com.lifepill.possystem.client;

import com.lifepill.possystem.client.dto.MicroserviceApiResponse;
import com.lifepill.possystem.client.dto.MicroserviceEmployeeDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Fallback implementation for Identity Service Client
 * Provides default responses when the Identity Service is unavailable
 */
@Component
public class IdentityServiceClientFallback implements IdentityServiceClient {

    @Override
    public ResponseEntity<MicroserviceApiResponse<MicroserviceEmployeeDTO>> getEmployeeById(Long employerId) {
        MicroserviceApiResponse<MicroserviceEmployeeDTO> response = new MicroserviceApiResponse<>();
        response.setCode(503);
        response.setMessage("Identity Service unavailable");
        response.setData(createDefaultEmployee(employerId));
        response.setSuccess(false);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<MicroserviceApiResponse<List<MicroserviceEmployeeDTO>>> getEmployeesByBranch(Long branchId) {
        MicroserviceApiResponse<List<MicroserviceEmployeeDTO>> response = new MicroserviceApiResponse<>();
        response.setCode(503);
        response.setMessage("Identity Service unavailable");
        response.setData(Collections.emptyList());
        response.setSuccess(false);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<MicroserviceApiResponse<MicroserviceEmployeeDTO>> getManagerByBranch(Long branchId) {
        MicroserviceApiResponse<MicroserviceEmployeeDTO> response = new MicroserviceApiResponse<>();
        response.setCode(503);
        response.setMessage("Identity Service unavailable");
        response.setData(null);
        response.setSuccess(false);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<MicroserviceApiResponse<Boolean>> employeeExists(Long employerId) {
        MicroserviceApiResponse<Boolean> response = new MicroserviceApiResponse<>();
        response.setCode(503);
        response.setMessage("Identity Service unavailable - assuming employee exists");
        response.setData(true); // Assume exists when service is down
        response.setSuccess(false);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<MicroserviceApiResponse<Long>> countEmployeesByBranch(Long branchId) {
        MicroserviceApiResponse<Long> response = new MicroserviceApiResponse<>();
        response.setCode(503);
        response.setMessage("Identity Service unavailable");
        response.setData(0L);
        response.setSuccess(false);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<MicroserviceApiResponse<MicroserviceEmployeeDTO>> getEmployeeByEmail(String email) {
        MicroserviceApiResponse<MicroserviceEmployeeDTO> response = new MicroserviceApiResponse<>();
        response.setCode(503);
        response.setMessage("Identity Service unavailable - cannot authenticate");
        response.setData(null);
        response.setSuccess(false);
        return ResponseEntity.ok(response);
    }

    private MicroserviceEmployeeDTO createDefaultEmployee(Long employerId) {
        MicroserviceEmployeeDTO employee = new MicroserviceEmployeeDTO();
        employee.setEmployerId(employerId);
        employee.setEmployerFirstName("Unknown");
        employee.setEmployerLastName("Employee");
        return employee;
    }
}
