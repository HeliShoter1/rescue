package com.rescue.rescue.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceAlreadyExistsException extends BaseException {

    public ResourceAlreadyExistsException() {
        super("Tài nguyên đã tồn tại", HttpStatus.CONFLICT, "RESOURCE_ALREADY_EXISTS");
    }

    public ResourceAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT, "RESOURCE_ALREADY_EXISTS");
    }
}
