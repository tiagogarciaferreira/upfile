package com.tgfcodes.upfile.domain.upload;

import com.tgfcodes.upfile.domain.Checks;
import com.tgfcodes.upfile.domain.exceptions.DomainValidationException;

public record LinkResult(
        String link,
        Integer ttlSeconds
) {
    public LinkResult {
        Checks.requireNonEmpty(link, () -> new DomainValidationException("Link cannot be empty"));
        Checks.requirePositive(ttlSeconds, () -> new DomainValidationException("TTL seconds must be positive"));
    }
}
