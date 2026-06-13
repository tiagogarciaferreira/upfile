package com.tgfcodes.upfile.presentation.file;

import com.tgfcodes.upfile.application.output.UploadFileOutput;
import com.tgfcodes.upfile.domain.Checks;
import com.tgfcodes.upfile.domain.exceptions.DomainValidationException;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

@Schema(description = "Response summary after a successful file ingest action")
public record UploadFileResponse(
        @Schema(description = "Tracking immutable system ID", example = "9b1deb4d-3b7d-4bad-9bdd-2b0d7b3dcb6d")
        UUID id,

        @Schema(description = "Sanitised storage name", example = "invoice_2026.pdf")
        String fileName,

        @Schema(description = "Calculated size payload in bytes", example = "1048576")
        Long size,

        @Schema(description = "Secure collision-resistant SHA-256 signature", example = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855")
        String hash,

        @Schema(description = "Timestamp of the file ingestion", example = "2026-06-12T23:23:40Z")
        Instant createdAt
) {
    public static UploadFileResponse from(UploadFileOutput uploadFileOutput) {
        Checks.requireNonNull(uploadFileOutput, () -> new DomainValidationException("UploadFileOutput cannot be null"));
        return new UploadFileResponse(
                uploadFileOutput.id(),
                uploadFileOutput.fileName(),
                uploadFileOutput.size(),
                uploadFileOutput.hash(),
                uploadFileOutput.createdAt()
        );
    }
}
