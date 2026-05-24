package com.tgfcodes.upfile.domain.storedfile;

import com.tgfcodes.upfile.domain.Checks;
import com.tgfcodes.upfile.domain.annotations.AppService;
import com.tgfcodes.upfile.domain.exceptions.DomainValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AppService
public class FileDeduplicationService {

    private static final Logger log = LoggerFactory.getLogger(FileDeduplicationService.class);

    private final StoredFiles storedFiles;

    public FileDeduplicationService(final StoredFiles storedFiles) {
        this.storedFiles = storedFiles;
    }

    public void ensureUniqueness(String fileHash) {
        Checks.requireNonEmpty(fileHash, () -> new DomainValidationException("File hash cannot be null"));

        if (storedFiles.existsByHash(fileHash)) {
            log.info("File with hash '{}' already exists", fileHash);
            throw new DuplicateFileException(fileHash);
        }
    }
}