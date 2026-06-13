package com.tgfcodes.upfile.presentation.auth;

import com.tgfcodes.upfile.application.output.LoginOutput;
import com.tgfcodes.upfile.domain.Checks;
import com.tgfcodes.upfile.domain.exceptions.DomainValidationException;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response containing the generated session access token details")
public record LoginResponse(

        @Schema(description = "The encrypted JSON Web Token (JWT) string", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String accessToken,

        @Schema(description = "Context classification category of the payload", example = "JWT")
        String type,

        @Schema(description = "The HTTP Authorization scheme name requested", example = "Bearer")
        String authType,

        @Schema(description = "Token expiration lifespan measured in seconds", example = "3600")
        Long expiresIn
) {
    public LoginResponse {
        Checks.requireNonEmpty(accessToken, () -> new DomainValidationException("Access token cannot be empty"));
        Checks.requireNonEmpty(type, () -> new DomainValidationException("Type cannot be empty"));
        Checks.requireNonEmpty(authType, () -> new DomainValidationException("Auth type cannot be empty"));
        Checks.requireNonNull(expiresIn, () -> new DomainValidationException("Expires in cannot be null"));
    }

    public static LoginResponse from(LoginOutput loginOutput) {
        Checks.requireNonNull(loginOutput, () -> new DomainValidationException("LoginOutput cannot be null"));
        return new LoginResponse(
                loginOutput.accessToken(),
                loginOutput.type(),
                loginOutput.authType(),
                loginOutput.expiresIn()
        );
    }
}