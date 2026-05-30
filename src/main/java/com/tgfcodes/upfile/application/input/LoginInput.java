package com.tgfcodes.upfile.application.input;

import com.tgfcodes.upfile.domain.Checks;
import com.tgfcodes.upfile.domain.exceptions.DomainValidationException;

public record LoginInput(
        String username,
        String password
) {
    public LoginInput {
        Checks.requireNonEmpty(username, () -> new DomainValidationException("Username is required"));
        Checks.requireNonEmpty(password, () -> new DomainValidationException("Password is required"));
    }
}