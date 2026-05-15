package com.rescue.rescue.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidInputException extends BaseException {

    public InvalidInputException() {
        super("Dữ liệu đầu vào không hợp lệ", HttpStatus.BAD_REQUEST, "INVALID_INPUT");
    }

    public InvalidInputException(String message) {
        super(message, HttpStatus.BAD_REQUEST, "INVALID_INPUT");
    }
}
