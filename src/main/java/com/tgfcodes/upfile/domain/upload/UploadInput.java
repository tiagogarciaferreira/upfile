package com.tgfcodes.upfile.domain.upload;

import com.tgfcodes.upfile.domain.Checks;
import com.tgfcodes.upfile.domain.exceptions.DomainValidationException;

import java.io.InputStream;

public record UploadInput(
        String fileName,
        String hash,
        String algorithm,
        String contentType,
        String contentDisposition,
        Long size,
        InputStream inputStream
) {
    public UploadInput {
        Checks.requireNonEmpty(fileName, () -> new DomainValidationException("File name cannot be empty"));
        Checks.requireNonEmpty(hash, () -> new DomainValidationException("Hash cannot be empty"));
        Checks.requireNonEmpty(contentType, () -> new DomainValidationException("Content type cannot be empty"));
        Checks.requireNonEmpty(contentDisposition, () -> new DomainValidationException("Content disposition cannot be empty"));
        Checks.requireNonNull(size, () -> new DomainValidationException("Size cannot be null"));
    }
}