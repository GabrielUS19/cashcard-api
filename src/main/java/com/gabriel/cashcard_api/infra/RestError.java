package com.gabriel.cashcard_api.infra;

import java.time.Instant;

public record RestError(
        String type,
        String title,
        int status,
        String detail,
        String instance,
        Instant timestamp
) {
}
