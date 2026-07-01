package com.gabriel.cashcard_api.dto.requests;

import java.math.BigDecimal;

public record CashcardRequest(BigDecimal amount) {
    public CashcardRequest {
        if (amount == null) {
            amount = BigDecimal.ZERO;
        }
    }
}
