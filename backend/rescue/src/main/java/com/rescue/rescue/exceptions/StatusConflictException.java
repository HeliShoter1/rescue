package com.rescue.rescue.exceptions;

import org.springframework.http.HttpStatus;

public class StatusConflictException extends BaseException {

    public StatusConflictException() {
        super("Trạng thái hiện tại không cho phép thực hiện hành động này", HttpStatus.CONFLICT, "STATUS_CONFLICT");
    }

    public StatusConflictException(String currentStatus, String action) {
        super("Không thể thực hiện '" + action + "' khi trạng thái đang là: " + currentStatus,
                HttpStatus.CONFLICT, "STATUS_CONFLICT");
    }

    public StatusConflictException(String message) {
        super(message, HttpStatus.CONFLICT, "STATUS_CONFLICT");
    }
}
