package com.gabriel.cashcard_api.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
