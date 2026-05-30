package com.tgfcodes.upfile.application.output;

import com.tgfcodes.upfile.domain.Checks;
import com.tgfcodes.upfile.domain.exceptions.DomainValidationException;

import java.io.InputStream;

public record FileDownloadOutput(
        String fileName,
        String contentType,
        String contentDisposition,
        Long size,
        InputStream stream
) {
    public FileDownloadOutput {
        Checks.requireNonEmpty(fileName, () -> new DomainValidationException("File name cannot be empty"));
        Checks.requireNonEmpty(contentType, () -> new DomainValidationException("Content type cannot be empty"));
        Checks.requireNonEmpty(contentDisposition, () -> new DomainValidationException("Content disposition cannot be empty"));
        Checks.requireNonNull(size, () -> new DomainValidationException("Size cannot be null"));
        Checks.requireNonNull(stream, () -> new DomainValidationException("Stream cannot be null"));
    }
}