package com.tgfcodes.upfile.infrastructure.storage;

import com.tgfcodes.upfile.domain.Checks;
import com.tgfcodes.upfile.domain.exceptions.DomainValidationException;
import com.tgfcodes.upfile.domain.upload.FileMetadataName;
import com.tgfcodes.upfile.domain.upload.UploadResult;
import org.jspecify.annotations.NullMarked;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.Map;
import java.util.UUID;

@NullMarked
@Component
public class UploadResultMapper {

    public UploadResult toUploadResult(PutObjectRequest request, String eTag) {

        Checks.requireNonNull(request, () -> new DomainValidationException("PutObjectRequest cannot be null"));
        Checks.requireNonEmpty(request.metadata(), () -> new DomainValidationException("Metadata cannot be empty"));
        Checks.requireNonEmpty(eTag, () -> new DomainValidationException("Etag cannot be empty"));

        Map<String, String> metadata = request.metadata();

        return new UploadResult(
                UUID.fromString(metadata.getOrDefault(FileMetadataName.ENTITY_ID.getValue(), "")),
                request.bucket(),
                metadata.getOrDefault(FileMetadataName.ORIGINAL_FILENAME.getValue(), ""),
                request.key(),
                eTag,
                request.checksumXXHASH3(),
                metadata.getOrDefault(FileMetadataName.FILE_EXTENSION.getValue(), ""),
                request.contentType(),
                request.contentDisposition(),
                request.contentLength(),
                metadata.getOrDefault(FileMetadataName.MIME_TYPE.getValue(), ""),
                metadata.getOrDefault(FileMetadataName.MEDIA_TYPE.getValue(), "")
        );
    }
}