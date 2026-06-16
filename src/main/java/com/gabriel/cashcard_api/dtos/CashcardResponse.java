package com.gabriel.cashcard_api.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record CashcardResponse(
        UUID id,
        BigDecimal amount) {
}
