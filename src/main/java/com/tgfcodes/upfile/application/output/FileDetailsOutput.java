package com.tgfcodes.upfile.application.output;

import com.tgfcodes.upfile.domain.Checks;
import com.tgfcodes.upfile.domain.exceptions.DomainValidationException;
import com.tgfcodes.upfile.domain.storedfile.StoredFile;

import java.time.Instant;
import java.util.UUID;

public record FileDetailsOutput(
        UUID id,
        String bucket,
        String fileName,
        String key,
        String eTag,
        String hash,
        String extension,
        String contentType,
        String contentDisposition,
        Long size,
        String mimeType,
        String type,
        Instant createdAt
) {
    public FileDetailsOutput {
        Checks.requireNonNull(id, () -> new DomainValidationException("Id cannot be null"));
        Checks.requireNonEmpty(fileName, () -> new DomainValidationException("File name cannot be empty"));
        Checks.requireNonEmpty(hash, () -> new DomainValidationException("Hash cannot be empty"));
        Checks.requireNonNull(size, () -> new DomainValidationException("Size cannot be null"));
        Checks.requireNonNull(createdAt, () -> new DomainValidationException("Created at cannot be null"));
    }

    public static FileDetailsOutput from(StoredFile storedFile) {
        Checks.requireNonNull(storedFile, () -> new DomainValidationException("Stored file cannot be null"));
        return new FileDetailsOutput(
                storedFile.id(),
                storedFile.bucket(),
                storedFile.fileName(),
                storedFile.key(),
                storedFile.eTag(),
                storedFile.hash(),
                storedFile.extension(),
                storedFile.contentType(),
                storedFile.contentDisposition(),
                storedFile.size(),
                storedFile.mimeType(),
                storedFile.type(),
                storedFile.createdAt()
        );
    }
}