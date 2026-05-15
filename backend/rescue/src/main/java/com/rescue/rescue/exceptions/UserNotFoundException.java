package com.rescue.rescue.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BaseException {

    public UserNotFoundException() {
        super("Không tìm thấy người dùng", HttpStatus.NOT_FOUND, "USER_NOT_FOUND");
    }

    public UserNotFoundException(Long id) {
        super("Không tìm thấy người dùng với id: " + id, HttpStatus.NOT_FOUND, "USER_NOT_FOUND");
    }

    public UserNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, "USER_NOT_FOUND");
    }
}
