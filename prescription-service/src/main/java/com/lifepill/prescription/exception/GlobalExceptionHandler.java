package com.lifepill.prescription.exception;

import com.lifepill.prescription.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleRuntimeException(RuntimeException e) {
        log.error("Runtime Exception: ", e);
        
        if (e.getMessage().contains("Branch has already responded")) {
            return new ResponseEntity<>(
                    ApiResponse.error("DUPLICATE_RESPONSE", e.getMessage()),
                    HttpStatus.CONFLICT
            );
        }
        
        if (e.getMessage().contains("Prescription not found") || e.getMessage().contains("Response not found")) {
            return new ResponseEntity<>(
                    ApiResponse.error("NOT_FOUND", e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }

        return new ResponseEntity<>(
                ApiResponse.error("INTERNAL_ERROR", e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
