package com.tgfcodes.upfile.domain;

import java.util.UUID;

public class StoredFileNotFoundException extends DomainException {

    public StoredFileNotFoundException(UUID id) {
        super(String.format("Stored file with id '%s' not found", id));
    }
}
