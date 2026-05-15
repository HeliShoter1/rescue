package com.rescue.rescue.exceptions;

import org.springframework.http.HttpStatus;

public class OperationNotAllowedException extends BaseException {

    public OperationNotAllowedException() {
        super("Hành động không được phép", HttpStatus.FORBIDDEN, "OPERATION_NOT_ALLOWED");
    }

    public OperationNotAllowedException(String message) {
        super(message, HttpStatus.FORBIDDEN, "OPERATION_NOT_ALLOWED");
    }
}
