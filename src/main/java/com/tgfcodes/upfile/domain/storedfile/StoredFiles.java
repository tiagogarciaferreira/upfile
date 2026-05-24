package com.tgfcodes.upfile.domain.storedfile;

import java.util.Optional;
import java.util.UUID;

public interface StoredFiles {

    StoredFile save(StoredFile storedFile);

    Optional<StoredFile> findById(UUID id);

    boolean existsByHash(String hash);

    void deleteById(UUID id);
}
