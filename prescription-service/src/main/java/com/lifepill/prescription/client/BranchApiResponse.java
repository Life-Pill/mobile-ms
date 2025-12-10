package com.lifepill.prescription.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response wrapper matching the structure returned by Branch Service.
 * Branch Service returns: {"code": 200, "message": "...", "data": ...}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchApiResponse<T> {
    private int code;
    private String message;
    private T data;
}
