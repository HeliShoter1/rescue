package com.rescue.rescue.exceptions;

import org.springframework.http.HttpStatus;

public class InternalServerException extends BaseException {

    public InternalServerException() {
        super("Lỗi hệ thống, vui lòng thử lại sau", HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR");
    }

    public InternalServerException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR");
    }
}
