package com.gabriel.cashcard_api.infra.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StrongPasswordValidation implements ConstraintValidator<StrongPassword, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        if (value.length() < 8 || value.length() > 30) {
            return false;
        }

        var hasUppercase = false;
        var hasDigit = false;
        var hasSpecial = false;

        for (char c : value.toCharArray()) {
            if (Character.isWhitespace(c)) return false;
            if (Character.isUpperCase(c)) hasUppercase = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else if (Character.isLowerCase(c));
            else hasSpecial = true;
        }

        return hasUppercase && hasDigit && hasSpecial;
    }
}
