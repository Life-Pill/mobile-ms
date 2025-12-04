package com.lifepill.identityservice.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Standard response wrapper for API responses.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StandardResponse {
    
    private int code;
    
    private String message;
    
    private Object data;
}
