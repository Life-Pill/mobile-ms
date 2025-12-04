package com.lifepill.identityservice.exception;

/**
 * Exception thrown when attempting to create a duplicate entity.
 */
public class EntityDuplicationException extends RuntimeException {
    
    public EntityDuplicationException(String message) {
        super(message);
    }
}
