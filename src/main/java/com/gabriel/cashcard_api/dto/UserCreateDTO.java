package com.gabriel.cashcard_api.dto;

import com.gabriel.cashcard_api.infra.validate.FieldsValueMatch;
import com.gabriel.cashcard_api.infra.validate.StrongPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@FieldsValueMatch(
        message = "Passwords do not match",
        field = "password",
        fieldMatch = "confirmPassword"
)
public record UserCreateDTO(
        @NotBlank(message = "Field name is required")
        String name,

        @NotBlank(message = "Field email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Field password is required")
        @StrongPassword
        String password,

        String confirmPassword
) {
}
