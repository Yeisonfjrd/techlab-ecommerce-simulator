package com.techlab.ecommerce.common.error;

/** Valid request that breaks a store rule (no stock, email taken). 409. */
public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String message) {
        super(message);
    }
}
