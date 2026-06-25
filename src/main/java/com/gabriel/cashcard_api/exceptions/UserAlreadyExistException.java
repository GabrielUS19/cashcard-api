package com.gabriel.cashcard_api.exceptions;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistException extends BaseException {
    public UserAlreadyExistException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
