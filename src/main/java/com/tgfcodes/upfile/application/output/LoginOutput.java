package com.tgfcodes.upfile.application.output;

import com.tgfcodes.upfile.domain.Checks;
import com.tgfcodes.upfile.domain.exceptions.DomainValidationException;

public record LoginOutput(
        String accessToken,
        String type,
        String authType,
        Long expiresIn
) {
    public LoginOutput {
        Checks.requireNonEmpty(accessToken, () -> new DomainValidationException("Access token cannot be empty"));
        Checks.requireNonEmpty(type, () -> new DomainValidationException("Type cannot be empty"));
        Checks.requireNonEmpty(authType, () -> new DomainValidationException("Auth type cannot be empty"));
        Checks.requireNonNull(expiresIn, () -> new DomainValidationException("Expires in cannot be null"));
    }
}