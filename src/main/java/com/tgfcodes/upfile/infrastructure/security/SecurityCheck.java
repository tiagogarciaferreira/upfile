package com.tgfcodes.upfile.infrastructure.security;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@NullMarked
@Component
public class SecurityCheck {

    public UUID getAuthenticatedUserId() {
        Jwt principal = getPrincipal();
        return UUID.fromString(Objects.requireNonNull(principal).getSubject());
    }

    private @Nullable Jwt getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Jwt) Objects.requireNonNull(authentication).getPrincipal();
    }
}