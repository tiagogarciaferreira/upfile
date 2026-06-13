package com.tgfcodes.upfile.presentation.file;

import com.tgfcodes.upfile.application.output.FileDetailsOutput;
import com.tgfcodes.upfile.domain.Checks;
import com.tgfcodes.upfile.domain.exceptions.DomainValidationException;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

@Schema(description = "Detailed operational record information metadata payload")
public record FileDetailsResponse(
        @Schema(description = "Tracking immutable system ID", example = "9b1deb4d-3b7d-4bad-9bdd-2b0d7b3dcb6d")
        UUID id,

        @Schema(description = "Sanitised storage name", example = "report.xlsx")
        String fileName,

        @Schema(description = "Computed extension extraction token", example = ".png")
        String extension,

        @Schema(description = "Standard dynamic internet media content mapping", example = "image/png")
        String contentType,

        @Schema(description = "Allocation footprint size metric in bytes", example = "5242880")
        long size,

        @Schema(description = "Secure collision-resistant SHA-256 signature", example = "a20bc44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b1234")
        String hash,

        @Schema(description = "Internal categorisation group parameter", example = "image")
        String type,

        @Schema(description = "Timestamp when transactional index was created")
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
