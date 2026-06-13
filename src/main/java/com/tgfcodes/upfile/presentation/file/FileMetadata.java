package com.tgfcodes.upfile.presentation.file;

import com.tgfcodes.upfile.application.query.FileMetadataOutput;
import com.tgfcodes.upfile.domain.Checks;
import com.tgfcodes.upfile.domain.exceptions.DomainValidationException;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

@Schema(description = "Metadata record representation stored inside the transactional search index")
public record FileMetadata(

        @Schema(description = "Immutable unique identifier of the file", example = "9b1deb4d-3b7d-4bad-9bdd-2b0d7b3dcb6d")
        UUID id,

        @Schema(description = "Original sanitized name of the uploaded file", example = "financial_report.pdf")
        String fileName,

        @Schema(description = "Extracted file extension token", example = "pdf")
        String extension,

        @Schema(description = "Standard dynamic internet media content mapping", example = "image/png")
        String contentType,

        @Schema(description = "File footprint size computed in bytes", example = "2048576")
        Long size,

        @Schema(description = "Secure collision-resistant SHA-256 signature hash", example = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855")
        String hash,

        @Schema(description = "Internal architectural categorization type group", example = "image")
        String type,

        @Schema(description = "ISO timestamp indicating when the tracking index node was created")
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