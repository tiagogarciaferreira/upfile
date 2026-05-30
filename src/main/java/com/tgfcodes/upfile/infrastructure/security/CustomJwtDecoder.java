package com.tgfcodes.upfile.infrastructure.security;

import com.nimbusds.jose.crypto.Ed25519Verifier;
import com.nimbusds.jose.jwk.OctetKeyPair;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@NullMarked
@RequiredArgsConstructor
public class CustomJwtDecoder implements JwtDecoder {

    private final OctetKeyPair jwtSigningKey;

    @SneakyThrows
    @Override
    public Jwt decode(String token) {
        Ed25519Verifier verifier = new Ed25519Verifier(jwtSigningKey.toPublicJWK());
        SignedJWT signedJWT = SignedJWT.parse(token);

        if (!signedJWT.verify(verifier)) throw new BadJwtException("Invalid EdDSA signature");

        JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

        Instant issuedAt = Optional.ofNullable(claims.getIssueTime())
                .map(Date::toInstant)
                .orElse(Instant.MIN);

        Instant expiresAt = Optional.ofNullable(claims.getExpirationTime())
                .map(Date::toInstant)
                .orElse(Instant.MIN);

        if (Instant.now().isAfter(expiresAt)) throw new BadJwtException("JWT token has expired");

        return Jwt.withTokenValue(token)
                .issuer(claims.getIssuer())
                .notBefore(claims.getNotBeforeTime().toInstant())
                .audience(claims.getAudience())
                .jti(claims.getJWTID())
                .headers(headers -> headers.putAll(signedJWT.getHeader().toJSONObject()))
                .claims(c -> c.putAll(claims.getClaims()))
                .subject(claims.getSubject())
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .build();
    }
}
