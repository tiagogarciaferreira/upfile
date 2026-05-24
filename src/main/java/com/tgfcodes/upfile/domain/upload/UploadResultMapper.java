package com.tgfcodes.upfile.domain.upload;

import org.jspecify.annotations.NullMarked;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@NullMarked
@Component
public class UploadResultMapper {

    public UploadResult toUploadResult(PutObjectResponse putObjectResponse) {
        return new UploadResult(null, null, null);
    }
}