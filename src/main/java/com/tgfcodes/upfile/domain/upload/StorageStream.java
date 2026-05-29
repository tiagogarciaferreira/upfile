package com.tgfcodes.upfile.domain.upload;

import com.tgfcodes.upfile.domain.Checks;
import com.tgfcodes.upfile.domain.exceptions.DomainValidationException;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.InputStream;

public record StorageStream(
        InputStream stream
) {
    public static StorageStream from(ResponseInputStream<GetObjectResponse> response) {
        Checks.requireNonNull(response, () -> new DomainValidationException("ResponseInputStream cannot be null"));
        return new StorageStream(response);
    }
}