package com.techlab.ecommerce.common.error;

/**
 * The request is well formed but breaks a rule of the store (not enough stock, email taken...).
 * Mapped to 409 Conflict: the request conflicts with the current state of the data.
 */
public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String message) {
        super(message);
    }
}
