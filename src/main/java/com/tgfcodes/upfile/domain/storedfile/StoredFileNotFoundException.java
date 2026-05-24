package com.tgfcodes.upfile.domain.storedfile;

import com.tgfcodes.upfile.domain.exceptions.DomainException;

import java.util.UUID;

public class StoredFileNotFoundException extends DomainException {

    public StoredFileNotFoundException(UUID id) {
        super(String.format("Stored file with id '%s' not found", id));
    }
}
