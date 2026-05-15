package com.rescue.rescue.exception;

import org.springframework.http.HttpStatus;

public class AccessDeniedException extends BaseException {

    public AccessDeniedException() {
        super("Bạn không có quyền thực hiện hành động này", HttpStatus.FORBIDDEN, "ACCESS_DENIED");
    }

    public AccessDeniedException(String message) {
        super(message, HttpStatus.FORBIDDEN, "ACCESS_DENIED");
    }
}
