package com.tgfcodes.upfile.domain;

import com.tgfcodes.upfile.domain.annotations.AppService;
import com.tgfcodes.upfile.domain.exceptions.DuplicateFileException;

import java.io.InputStream;
import java.util.Objects;

@AppService
public class FileDeduplicationService {

    private final StoredFiles storedFiles;

    private final FileHashCalculator fileHashCalculator;

    public FileDeduplicationService(final StoredFiles storedFiles, final FileHashCalculator fileHashCalculator) {
        this.storedFiles = storedFiles;
        this.fileHashCalculator = fileHashCalculator;
    }

    public String processUpload(InputStream inputStream) {
        Objects.requireNonNull(inputStream, "Input stream cannot be null");
        String hash = fileHashCalculator.calculateHash(inputStream);
        if (storedFiles.existsByHash(hash)) throw new DuplicateFileException(hash);
        return hash;
    }
}