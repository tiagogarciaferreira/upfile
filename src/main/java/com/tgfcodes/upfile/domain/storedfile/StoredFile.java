package com.tgfcodes.upfile.domain.storedfile;

import com.tgfcodes.upfile.domain.Checks;
import com.tgfcodes.upfile.domain.exceptions.DomainValidationException;

import java.time.Instant;
import java.util.UUID;

public record StoredFile(
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

    public static StoredFile create(
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
            String type
    ) {
        return new StoredFile(
                id,
                bucket,
                fileName,
                key,
                eTag,
                hash,
                extension,
                contentType,
                contentDisposition,
                size,
                mimeType,
                type,
                Instant.now()
        );
    }

    public static StoredFile existing(
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
        return new StoredFile(
                id,
                bucket,
                fileName,
                key,
                eTag,
                hash,
                extension,
                contentType,
                contentDisposition,
                size,
                mimeType,
                type,
                createdAt
        );
    }

    public StoredFile {
        Checks.requireNonNull(id, () -> new DomainValidationException("Id cannot be null"));
        Checks.requireNonEmpty(bucket, () -> new DomainValidationException("Bucket cannot be empty"));
        Checks.requireNonEmpty(fileName, () -> new DomainValidationException("File name cannot be empty"));
        Checks.requireNonEmpty(key, () -> new DomainValidationException("Key cannot be empty"));
        Checks.requireNonEmpty(eTag, () -> new DomainValidationException("Etag cannot be empty"));
        Checks.requireNonEmpty(hash, () -> new DomainValidationException("Hash cannot be empty"));
        Checks.requireNonEmpty(extension, () -> new DomainValidationException("Extension cannot be empty"));
        Checks.requireNonEmpty(contentType, () -> new DomainValidationException("Content type cannot be empty"));
        Checks.requireNonEmpty(contentDisposition, () -> new DomainValidationException("Content disposition cannot be empty"));
        Checks.requireNonNull(size, () -> new DomainValidationException("Size cannot be null"));
        Checks.requireNonEmpty(mimeType, () -> new DomainValidationException("MimeType cannot be empty"));
        Checks.requireNonEmpty(type, () -> new DomainValidationException("Type cannot be empty"));
        Checks.requireNonNull(createdAt, () -> new DomainValidationException("Created at cannot be null"));
    }
}