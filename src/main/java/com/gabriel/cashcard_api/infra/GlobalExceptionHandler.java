package com.gabriel.cashcard_api.infra;

import com.gabriel.cashcard_api.exceptions.CashcardNotFoundException;
import com.gabriel.cashcard_api.exceptions.UserAlreadyExistException;
import com.gabriel.cashcard_api.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<RestError> handleUserAlreadyExistException(
            UserAlreadyExistException ex,
            HttpServletRequest request
    ) {
        int status = ex.getStatus().value();

        var errorResponse = new RestError(
                "about:blank",
                "Invalid Fields Value",
                status,
                ex.getMessage(),
                request.getRequestURI(),
                Instant.now()
        );

        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<RestError> handleUserNotFoundException(
            UserNotFoundException ex,
            HttpServletRequest request
    ) {
        int status = ex.getStatus().value();

        var errorResponse = new RestError(
                "about:blank",
                "User not Found",
                status,
                ex.getMessage(),
                request.getRequestURI(),
                Instant.now()
        );

        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<RestError> handleUsernameNotFoundException(
            UsernameNotFoundException ex,
            HttpServletRequest request
    ) {
        int status = HttpStatus.NOT_FOUND.value();

        var errorResponse = new RestError(
                "about:blank",
                "User not Found",
                status,
                ex.getMessage(),
                request.getRequestURI(),
                Instant.now()
        );

        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<RestError> handleBadCredentialsException(
            BadCredentialsException ex,
            HttpServletRequest request
    ) {
        int status = HttpStatus.UNAUTHORIZED.value();

        var errorResponse = new RestError(
                "about:blank",
                "Bad credentials",
                status,
                "Invalid email or password",
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

    @Override
    protected @Nullable ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var servletRequest = ((ServletWebRequest) request).getRequest();

        var errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new RestError.ValidationError(error.getField(), error.getDefaultMessage()))
                .toList();

        var errorResponse = new RestError(
                "about:blank",
                "Invalid Fields Value",
                status.value(),
                "Values provided are not valid",
                servletRequest.getRequestURI(),
                Instant.now(),
                errors
        );

        return ResponseEntity.status(status.value()).body(errorResponse);
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

        ex.printStackTrace();

        return ResponseEntity.status(status).body(errorResponse);
    }
}
