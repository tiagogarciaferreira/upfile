package com.tgfcodes.upfile.application.usecase;

import com.tgfcodes.upfile.application.annotations.AppTransactional;
import com.tgfcodes.upfile.application.output.DownloadLinkOutput;
import com.tgfcodes.upfile.domain.Checks;
import com.tgfcodes.upfile.domain.annotations.AppService;
import com.tgfcodes.upfile.domain.exceptions.DomainValidationException;
import com.tgfcodes.upfile.domain.storedfile.StoredFile;
import com.tgfcodes.upfile.domain.storedfile.StoredFileNotFoundException;
import com.tgfcodes.upfile.domain.storedfile.StoredFiles;
import com.tgfcodes.upfile.domain.upload.LinkResult;
import com.tgfcodes.upfile.domain.upload.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@AppService
public class DownloadLinkUseCase {

    private static final Logger log = LoggerFactory.getLogger(DownloadLinkUseCase.class);

    private final StoredFiles storedFiles;

    private final Storage storage;

    public DownloadLinkUseCase(final StoredFiles storedFiles, final Storage storage) {
        this.storedFiles = storedFiles;
        this.storage = storage;
    }

    @AppTransactional(readOnly = true)
    public DownloadLinkOutput execute(UUID fileId) {
        Checks.requireNonNull(fileId, () -> new DomainValidationException("File id cannot be null"));

        StoredFile storedFile = storedFiles.findById(fileId).orElseThrow(() -> new StoredFileNotFoundException(fileId));
        LinkResult linkResult = storage.downloadLink(storedFile.bucket(), storedFile.key());

        log.info("File '{}' download link '{}'", fileId, linkResult.link());
        return new DownloadLinkOutput(
                storedFile.id(),
                linkResult.link(),
                linkResult.ttlSeconds()
        );
    }
}