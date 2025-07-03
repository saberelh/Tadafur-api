package com.project.Tadafur_api.shared.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceNotFoundException(String entity, String field, Object value) {
        super(String.format("%s not found with %s: %s", entity, field, value));
    }
}
