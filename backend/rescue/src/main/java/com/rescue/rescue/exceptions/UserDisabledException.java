package com.rescue.rescue.exceptions;

import org.springframework.http.HttpStatus;

public class UserDisabledException extends BaseException {

    public UserDisabledException() {
        super("Tài khoản đã bị vô hiệu hóa", HttpStatus.FORBIDDEN, "USER_DISABLED");
    }

    public UserDisabledException(String message) {
        super(message, HttpStatus.FORBIDDEN, "USER_DISABLED");
    }
}
