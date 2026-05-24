package com.tgfcodes.upfile.domain.storedfile;

import com.tgfcodes.upfile.domain.exceptions.DomainException;

public class DuplicateFileException extends DomainException {

    public DuplicateFileException(String message) {
        super(message);
    }
}