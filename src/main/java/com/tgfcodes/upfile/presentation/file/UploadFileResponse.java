package com.tgfcodes.upfile.presentation.file;

import com.tgfcodes.upfile.application.output.UploadFileOutput;
import com.tgfcodes.upfile.domain.Checks;
import com.tgfcodes.upfile.domain.exceptions.DomainValidationException;

import java.time.Instant;
import java.util.UUID;

public record UploadFileResponse(
        UUID id,
        String fileName,
        Long size,
        String hash,
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
