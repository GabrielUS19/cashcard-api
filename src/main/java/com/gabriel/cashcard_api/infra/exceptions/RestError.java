package com.gabriel.cashcard_api.infra.exceptions;

import java.time.Instant;
import java.util.List;

public record RestError(
        String type,
        String title,
        int status,
        String detail,
        String instance,
        Instant timestamp,
        List<ValidationError> errors
) {
    public RestError {
        errors = (errors != null) ? List.copyOf(errors) : List.of();
    }

    public RestError(String type,
                     String title,
                     int status,
                     String detail,
                     String instance,
                     Instant timestamp) {
        this(type, title, status, detail, instance, timestamp, List.of());
    }

    public record ValidationError(String field, String message) {}
}
