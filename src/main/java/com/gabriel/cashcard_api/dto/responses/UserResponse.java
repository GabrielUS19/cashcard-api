package com.gabriel.cashcard_api.dto.responses;

import java.util.UUID;

public record UserResponse(UUID id, String name, String email) {
}
