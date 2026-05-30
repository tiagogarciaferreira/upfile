package com.tgfcodes.upfile.presentation.file;

import com.tgfcodes.upfile.application.output.FileDetailsOutput;
import com.tgfcodes.upfile.domain.Checks;
import com.tgfcodes.upfile.domain.exceptions.DomainValidationException;

import java.time.Instant;
import java.util.UUID;

public record FileDetailsResponse(
        UUID id,
        String fileName,
        String extension,
        String contentType,
        long size,
        String hash,
        String type,
        Instant createdAt
) {

    public static FileDetailsResponse from(FileDetailsOutput fileDetailsOutput) {
        Checks.requireNonNull(fileDetailsOutput, () -> new DomainValidationException("FileDetailsOutput cannot be null"));
        return new FileDetailsResponse(
                fileDetailsOutput.id(),
                fileDetailsOutput.fileName(),
                fileDetailsOutput.extension(),
                fileDetailsOutput.contentType(),
                fileDetailsOutput.size(),
                fileDetailsOutput.hash(),
                fileDetailsOutput.type(),
                fileDetailsOutput.createdAt()
        );
    }
}
