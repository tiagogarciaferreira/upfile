package com.tgfcodes.upfile.application;

import com.tgfcodes.upfile.application.annotations.AppTransactional;
import com.tgfcodes.upfile.domain.annotations.AppService;
import com.tgfcodes.upfile.domain.storedfile.StoredFile;
import com.tgfcodes.upfile.domain.storedfile.StoredFileNotFoundException;
import com.tgfcodes.upfile.domain.storedfile.StoredFiles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@AppService
public class GetFileDetailsUseCase {

    private static final Logger log = LoggerFactory.getLogger(GetFileDetailsUseCase.class);

    private final StoredFiles storedFiles;

    public GetFileDetailsUseCase(final StoredFiles storedFiles) {
        this.storedFiles = storedFiles;
    }

    @AppTransactional(readOnly = true)
    public FileDetailsOutput execute(UUID fileId) {
        StoredFile storedFile = storedFiles.findById(fileId).orElseThrow(() -> new StoredFileNotFoundException(fileId));
        log.info("File '{}' details retrieved successfully", fileId);
        return FileDetailsOutput.from(storedFile);
    }
}
