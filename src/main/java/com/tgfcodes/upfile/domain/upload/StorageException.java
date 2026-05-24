package com.tgfcodes.upfile.domain.upload;

import com.tgfcodes.upfile.domain.exceptions.DomainException;

public class StorageException extends DomainException {

    public StorageException(String message) {
        super(message);
    }
}
