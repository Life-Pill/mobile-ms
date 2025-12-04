package com.lifepill.identityservice.client.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic API response wrapper for microservice communication.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MicroserviceApiResponse<T> {
    
    @JsonAlias({"status", "code"})
    private int code;
    
    private String message;
    
    private T data;
}
