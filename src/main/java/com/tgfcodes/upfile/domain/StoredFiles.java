package com.tgfcodes.upfile.domain;

import java.util.Optional;
import java.util.UUID;

public interface StoredFiles {

    StoredFile save(StoredFile storedFile);

    Optional<StoredFile> findById(UUID id);

    Optional<StoredFile> findByHash(String hash);

    void deleteById(UUID id);
}
