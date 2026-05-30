package com.tgfcodes.upfile.infrastructure.config;

import com.tgfcodes.upfile.infrastructure.security.SecurityCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "dateTimeProvider", auditorAwareRef = "auditorProvider")
public class JpaAuditingConfig {

    private final SecurityCheck securityCheck;

    @Bean
    public DateTimeProvider dateTimeProvider() {
        return () -> Optional.of(Instant.now().truncatedTo(ChronoUnit.MILLIS));
    }

    @Bean
    public AuditorAware<UUID> auditorProvider() {
        return () -> Optional.of(securityCheck.getAuthenticatedUserId());
    }
}