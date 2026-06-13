package com.tgfcodes.upfile.presentation.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.jspecify.annotations.NullMarked;

@NullMarked
@Schema(description = "Payload required to authenticate a user and generate a session token")
public record LoginRequest(

        @Schema(
                description = "The unique username or handle of the user",
                example = "john_doe",
                minLength = 3,
                maxLength = 50,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        String username,

        @Schema(
                description = "The plain-text password for authentication",
                example = "P@ssword123!",
                minLength = 8,
                maxLength = 20,
                format = "password",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
        String password
) {

    private String maskUsername(String username) {
        if (username.isBlank()) return "***";

        String trimmed = username.trim();
        if (trimmed.length() <= 2) return "*".repeat(trimmed.length());

        int visiblePrefix = 2;
        int visibleSuffix = Math.min(1, trimmed.length() - visiblePrefix);

        String prefix = trimmed.substring(0, visiblePrefix);
        String suffix = trimmed.substring(trimmed.length() - visibleSuffix);

        return prefix + "*".repeat(trimmed.length() - (visiblePrefix + visibleSuffix)) + suffix;
    }

    @Override
    public String toString() {
        return this.maskUsername(this.username());
    }
}