package com.tgfcodes.upfile.domain.upload;

import java.util.UUID;

public record UploadRetrieve(
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
}
