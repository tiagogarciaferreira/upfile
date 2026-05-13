package com.tgfcodes.upfile.domain;

import java.util.UUID;

public interface StoredFiles {

    StoredFile save(StoredFile storedFile);

    StoredFile findById(UUID id);

    StoredFile findByHash(String hash);

    void deleteById(UUID id);
}
