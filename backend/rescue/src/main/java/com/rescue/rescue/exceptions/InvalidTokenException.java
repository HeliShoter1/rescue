package com.rescue.rescue.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidTokenException extends BaseException {

    public InvalidTokenException() {
        super("Token không hợp lệ", HttpStatus.UNAUTHORIZED, "TOKEN_INVALID");
    }

    public InvalidTokenException(String message) {
        super(message, HttpStatus.UNAUTHORIZED, "TOKEN_INVALID");
    }
}
