package com.tgfcodes.upfile.domain.upload;

import com.tgfcodes.upfile.domain.exceptions.DomainException;

public class FileNotFoundException extends DomainException {

    public FileNotFoundException(String fileKey) {
        super(String.format("File with key '%s' not found", fileKey));
    }
}
