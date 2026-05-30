package com.tgfcodes.upfile.infrastructure.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "security.jwt")
@Data
public class JwtProperties {

    private String issuer;

    private String audience;

    private int expirationSeconds;

    private String publicKey;

    private String privateKey;
}