package com.tgfcodes.upfile.application.query;

import com.tgfcodes.upfile.domain.Checks;
import com.tgfcodes.upfile.domain.exceptions.DomainValidationException;
import com.tgfcodes.upfile.domain.storedfile.StoredFile;

import java.time.Instant;
import java.util.UUID;

public record FileMetadataOutput(
        UUID id,
        String fileName,
        String extension,
        String contentType,
        long size,
        String hash,
        String type,
        Instant createdAt
) {
    public FileMetadataOutput {
        Checks.requireNonNull(id, () -> new DomainValidationException("Id cannot be null"));
    }

    public static FileMetadataOutput from(StoredFile storedFile) {
        Checks.requireNonNull(storedFile, () -> new DomainValidationException("Stored file cannot be null"));
        return new FileMetadataOutput(
                storedFile.id(),
                storedFile.fileName(),
                storedFile.extension(),
                storedFile.contentType(),
                storedFile.size(),
                storedFile.hash(),
                storedFile.type(),
                storedFile.createdAt()
        );
    }
}