package com.tgfcodes.upfile.infrastructure.security;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.OctetKeyPair;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@NullMarked
@Configuration
@RequiredArgsConstructor
public class JwtSecurityConfig {

    @Value("${JWT_PUBLIC_KEY}")
    private Resource publicKeyResource;

    @Value("${JWT_PRIVATE_KEY}")
    private Resource privateKeyResource;

    @SneakyThrows
    @Bean
    public OctetKeyPair jwtSigningKey() {
        String privateJwkJson = new String(privateKeyResource.getContentAsByteArray(), UTF_8);
        String publicJwkJson = new String(publicKeyResource.getContentAsByteArray(), UTF_8);

        OctetKeyPair rawPrivate = OctetKeyPair.parse(privateJwkJson);
        String kid = OctetKeyPair.parse(publicJwkJson).computeThumbprint().toString();

        return new OctetKeyPair.Builder(Curve.Ed25519, rawPrivate.getX())
                .d(rawPrivate.getD())
                .keyID(kid)
                .algorithm(JWSAlgorithm.EdDSA)
                .keyUse(KeyUse.SIGNATURE)
                .build();
    }

    @Bean
    public JwtDecoder jwtDecoder(OctetKeyPair jwtSigningKey) {
        return new CustomJwtDecoder(jwtSigningKey);
    }
}