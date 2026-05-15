package com.rescue.rescue.exceptions;

import org.springframework.http.HttpStatus;

public class ExpiredTokenException extends BaseException {

    public ExpiredTokenException() {
        super("Token đã hết hạn", HttpStatus.UNAUTHORIZED, "TOKEN_EXPIRED");
    }

    public ExpiredTokenException(String message) {
        super(message, HttpStatus.UNAUTHORIZED, "TOKEN_EXPIRED");
    }
}
