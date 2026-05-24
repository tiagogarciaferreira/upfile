package com.tgfcodes.upfile.domain.upload;

import com.tgfcodes.upfile.domain.exceptions.DomainException;

public class FileTypeMismatchException extends DomainException {

    public FileTypeMismatchException(String message) {
        super(message);
    }
}
