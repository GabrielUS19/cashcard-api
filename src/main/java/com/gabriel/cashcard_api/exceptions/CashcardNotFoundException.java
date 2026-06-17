package com.gabriel.cashcard_api.exceptions;

import org.springframework.http.HttpStatus;

public class CashcardNotFoundException extends BaseException {
    public CashcardNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
