package com.tgfcodes.upfile.domain.upload;

import com.tgfcodes.upfile.domain.exceptions.DomainException;

public class BucketNotFoundException extends DomainException {

    public BucketNotFoundException(String bucket) {
        super(String.format("Bucket '%s' not found", bucket));
    }
}
