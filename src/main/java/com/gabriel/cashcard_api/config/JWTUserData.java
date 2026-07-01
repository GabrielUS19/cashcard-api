package com.gabriel.cashcard_api.config;

import lombok.Builder;

@Builder
public record JWTUserData(String userId, String email) {
}
