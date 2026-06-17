package com.gabriel.cashcard_api.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CashcardResponse(
        UUID id,
        BigDecimal amount) {
}
