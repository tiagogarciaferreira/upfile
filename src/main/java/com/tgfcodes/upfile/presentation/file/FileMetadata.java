package com.tgfcodes.upfile.presentation.file;

import com.tgfcodes.upfile.application.query.FileMetadataOutput;
import com.tgfcodes.upfile.domain.Checks;
import com.tgfcodes.upfile.domain.exceptions.DomainValidationException;

import java.time.Instant;
import java.util.UUID;

public record FileMetadata(
        UUID id,
        String fileName,
        String extension,
        String contentType,
        Long size,
        String hash,
        String type,
        Instant createdAt
) {
    public FileMetadata {
        Checks.requireNonNull(id, () -> new DomainValidationException("Id cannot be null"));
    }

    public static FileMetadata from(FileMetadataOutput fileMetadataOutput) {
        Checks.requireNonNull(fileMetadataOutput, () -> new DomainValidationException("FileMetadataOutput cannot be null"));
        return new FileMetadata(
                fileMetadataOutput.id(),
                fileMetadataOutput.fileName(),
                fileMetadataOutput.extension(),
                fileMetadataOutput.contentType(),
                fileMetadataOutput.size(),
                fileMetadataOutput.hash(),
                fileMetadataOutput.type(),
                fileMetadataOutput.createdAt()
        );
    }
}