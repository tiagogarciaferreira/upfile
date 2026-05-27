package com.tgfcodes.upfile.infrastructure.storage;

import com.tgfcodes.upfile.domain.Checks;
import com.tgfcodes.upfile.domain.exceptions.DomainValidationException;
import com.tgfcodes.upfile.domain.upload.UploadRetrieve;
import org.jspecify.annotations.NullMarked;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.util.UUID;

@NullMarked
@Component
public class UploadRetrieveMapper {

    public UploadRetrieve toUploadRetrieve(ResponseInputStream<GetObjectResponse> responseInputStream) {
        Checks.requireNonNull(responseInputStream, () -> new DomainValidationException("ResponseInputStream cannot be null"));
        Checks.requireNonNull(responseInputStream.response(), () -> new DomainValidationException("GetObjectResponse cannot be null"));

        GetObjectResponse putObjectResponse = responseInputStream.response();

        return new UploadRetrieve(
                UUID.randomUUID(),
                "",
                "",
                "",
                putObjectResponse.eTag(),
                putObjectResponse.checksumXXHASH3(),
                "",
                "",
                "",
                0L,
                "",
                ""
        );
    }
}