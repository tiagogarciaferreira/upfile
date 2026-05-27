package com.tgfcodes.upfile.application;

import com.tgfcodes.upfile.domain.Checks;
import com.tgfcodes.upfile.domain.exceptions.DomainValidationException;

import java.io.InputStream;
import java.util.concurrent.Callable;

public record UploadFileCommand(
        String fileName,
        String contentType,
        long size,
        Callable<InputStream> streamSupplier
) {
    public UploadFileCommand {
        Checks.requireNonEmpty(fileName, () -> new DomainValidationException("File name is required"));
        Checks.requireNonEmpty(contentType, () -> new DomainValidationException("Content type is required"));
        Checks.requireNonNull(streamSupplier, () -> new DomainValidationException("Stream supplier is required"));
        if (size <= 0) throw new DomainValidationException("File size must be greater than zero");
    }
}
