package com.tgfcodes.upfile.domain.upload;

import java.util.UUID;

public record UploadResult(
        UUID id,
        String key,
        String hash

) {
}
