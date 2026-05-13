package com.tgfcodes.upfile.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import static java.util.Objects.isNull;

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
            Long size
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
        Checks.requireNonNull(createdAt, () -> new DomainValidationException("Created at cannot be null"));
    }

    @Override
    public boolean equals(Object object) {
        if (isNull(object) || getClass() != object.getClass()) return false;
        StoredFile storedFile = (StoredFile) object;
        return Objects.equals(id, storedFile.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}