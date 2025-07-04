// File: shared/exception/BusinessValidationException.java
package com.project.Tadafur_api.shared.exception;

public class BusinessValidationException extends RuntimeException {

    public BusinessValidationException(String message) {
        super(message);
    }

    public BusinessValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}