package com.rescue.rescue.exceptions;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends BaseException {

    public UserAlreadyExistsException() {
        super("Người dùng đã tồn tại", HttpStatus.CONFLICT, "USER_ALREADY_EXISTS");
    }

    public UserAlreadyExistsException(String phoneNumber) {
        super("Số điện thoại " + phoneNumber + " đã được đăng ký", HttpStatus.CONFLICT, "USER_ALREADY_EXISTS");
    }
}
