package com.tgfcodes.upfile.application.usecase;

import com.tgfcodes.upfile.application.annotations.AppTransactional;
import com.tgfcodes.upfile.domain.Checks;
import com.tgfcodes.upfile.domain.annotations.AppService;
import com.tgfcodes.upfile.domain.storedfile.StoredFile;
import com.tgfcodes.upfile.domain.storedfile.StoredFileNotFoundException;
import com.tgfcodes.upfile.domain.storedfile.StoredFiles;
import com.tgfcodes.upfile.domain.upload.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@AppService
public class DeleteFileUseCase {

    private static final Logger log = LoggerFactory.getLogger(DeleteFileUseCase.class);

    private final StoredFiles storedFiles;

    private final Storage storage;

    public DeleteFileUseCase(final StoredFiles storedFiles, final Storage storage) {
        this.storedFiles = storedFiles;
        this.storage = storage;
    }

    @AppTransactional
    public void execute(UUID fileId) {
        Checks.requireNonNull(fileId, () -> new IllegalArgumentException("File id cannot be null"));
        StoredFile storedFile = storedFiles.findById(fileId).orElseThrow(() -> new StoredFileNotFoundException(fileId));
        storedFiles.deleteById(fileId);
        storage.delete(storedFile.bucket(), storedFile.key());
        log.info("File '{}' deleted successfully", storedFile.id());
    }
}
