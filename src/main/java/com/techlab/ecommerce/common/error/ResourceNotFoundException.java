package com.techlab.ecommerce.common.error;

/** Something the client asked for by id doesn't exist. Mapped to 404. */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resource, Object id) {
        super(resource + " " + id + " not found");
    }
}
