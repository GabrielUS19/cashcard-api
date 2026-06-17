package com.gabriel.cashcard_api.infra;

import com.gabriel.cashcard_api.exceptions.CashcardNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CashcardNotFoundException.class)
    public ResponseEntity<RestError> handleCashcardNotFoundException(
            CashcardNotFoundException ex,
            HttpServletRequest request) {
        int status = ex.getStatus().value();
        var errorResponse = new RestError(
                "about:blank",
                "Cash card not found",
                status,
                ex.getMessage(),
                request.getRequestURI(),
                Instant.now()
                );

        return ResponseEntity.status(status).body(errorResponse);
    }

    @Override
    protected @Nullable ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        var servletRequest = ((ServletWebRequest) request).getRequest();

        var errorResponse = new RestError(
                "about:blank",
                "Message Not Readable",
                status.value(),
                "Failed to read request: Invalid type of data in request body",
                servletRequest.getRequestURI(),
                Instant.now()
        );

        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestError> handleGeneralException(
            Exception ex, HttpServletRequest request
    ) {
        int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        var errorResponse = new RestError(
                "about:blank",
                "Internal Server Error",
                status,
                "Internal Server Error.",
                request.getRequestURI(),
                Instant.now()
        );

        return ResponseEntity.status(status).body(errorResponse);
    }
}
