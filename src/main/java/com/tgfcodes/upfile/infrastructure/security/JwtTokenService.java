package com.tgfcodes.upfile.infrastructure.security;

import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.Ed25519Signer;
import com.nimbusds.jose.jwk.OctetKeyPair;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.tgfcodes.upfile.application.LoginOutput;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.SECONDS;

@NullMarked
@RequiredArgsConstructor
@Service
public class JwtTokenService {

    private final JwtProperties jwtProperties;

    private final OctetKeyPair jwtSigningKey;

    @SneakyThrows
    public LoginOutput generateToken(UserAuth userAuth) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(jwtProperties.getExpirationSeconds(), SECONDS);

        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.EdDSA)
                .keyID(jwtSigningKey.getKeyID())
                .type(JOSEObjectType.JWT)
                .build();

        String scopes = userAuth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .jwtID(UUID.randomUUID().toString())
                .audience(jwtProperties.getAudience())
                .notBeforeTime(Date.from(issuedAt.minusSeconds(2L)))
                .issuer(jwtProperties.getIssuer())
                .subject(userAuth.getUserId().toString())
                .issueTime(Date.from(issuedAt))
                .expirationTime(Date.from(expiresAt))
                .claim("scope", scopes)
                .build();

        SignedJWT signedJWT = new SignedJWT(header, claims);
        signedJWT.sign(new Ed25519Signer(jwtSigningKey));

        return new LoginOutput(
                signedJWT.serialize(),
                "JWT",
                "Bearer",
                Duration.between(issuedAt, expiresAt).toSeconds()
        );
    }
}