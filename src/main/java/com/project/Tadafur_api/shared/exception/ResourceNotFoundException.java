// File: shared/exception/ResourceNotFoundException.java
package com.project.Tadafur_api.shared.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String entity, String field, Object value) {
        super(String.format("%s not found with %s: %s", entity, field, value));
    }
}
