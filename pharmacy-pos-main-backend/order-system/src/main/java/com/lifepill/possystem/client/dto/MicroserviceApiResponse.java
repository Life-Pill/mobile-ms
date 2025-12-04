package com.lifepill.possystem.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic API response wrapper for microservice communication
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MicroserviceApiResponse<T> {
    private int code;
    private String message;
    private T data;
    private boolean success;
}
