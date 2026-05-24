package com.tgfcodes.upfile.domain.upload;

import org.jspecify.annotations.NullMarked;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@NullMarked
@Component
public class UploadRetrieveMapper {

    public UploadRetrieve toUploadRetrieve(ResponseInputStream<GetObjectResponse> responseInputStream) {
        return new UploadRetrieve(null, null, null);
    }
}