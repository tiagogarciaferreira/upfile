package com.tgfcodes.upfile.presentation.file;

import com.tgfcodes.upfile.application.output.DownloadLinkOutput;
import com.tgfcodes.upfile.domain.Checks;
import com.tgfcodes.upfile.domain.exceptions.DomainValidationException;

import java.util.UUID;

public record DownloadLinkResponse(
        UUID id,
        String link,
        Integer ttlSeconds
) {
    public DownloadLinkResponse {
        Checks.requireNonNull(id, () -> new DomainValidationException("Id cannot be null"));
        Checks.requireNonEmpty(link, () -> new DomainValidationException("Link cannot be empty"));
        Checks.requireNonNull(ttlSeconds, () -> new DomainValidationException("TTL cannot be null"));
    }

    public static DownloadLinkResponse from(DownloadLinkOutput downloadLinkOutput) {
        Checks.requireNonNull(downloadLinkOutput, () -> new DomainValidationException("DownloadLinkOutput cannot be null"));
        return new DownloadLinkResponse(
                downloadLinkOutput.id(),
                downloadLinkOutput.link(),
                downloadLinkOutput.ttlSeconds()
        );
    }
}