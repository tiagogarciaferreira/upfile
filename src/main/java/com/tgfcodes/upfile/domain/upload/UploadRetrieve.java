package com.tgfcodes.upfile.domain.upload;

import java.util.UUID;

public record UploadRetrieve(
        UUID id,
        String key,
        String hash

) {
}
