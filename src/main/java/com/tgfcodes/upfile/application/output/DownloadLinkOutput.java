package com.tgfcodes.upfile.application.output;

import com.tgfcodes.upfile.domain.Checks;
import com.tgfcodes.upfile.domain.exceptions.DomainValidationException;

import java.util.UUID;

public record DownloadLinkOutput(
        UUID id,
        String link,
        Integer ttlSeconds
) {
    public DownloadLinkOutput {
        Checks.requireNonNull(id, () -> new DomainValidationException("Id cannot be null"));
        Checks.requireNonEmpty(link, () -> new DomainValidationException("Link cannot be empty"));
        Checks.requireNonNull(ttlSeconds, () -> new DomainValidationException("TTL cannot be null"));
    }
}