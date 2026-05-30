package com.tgfcodes.upfile.application.usecase;

import com.tgfcodes.upfile.application.annotations.AppTransactional;
import com.tgfcodes.upfile.application.input.UploadFileInput;
import com.tgfcodes.upfile.application.output.UploadFileOutput;
import com.tgfcodes.upfile.domain.annotations.AppService;
import com.tgfcodes.upfile.domain.storedfile.FileDeduplicationService;
import com.tgfcodes.upfile.domain.storedfile.FileHashCalculator;
import com.tgfcodes.upfile.domain.storedfile.StoredFile;
import com.tgfcodes.upfile.domain.storedfile.StoredFiles;
import com.tgfcodes.upfile.domain.upload.Storage;
import com.tgfcodes.upfile.domain.upload.StorageException;
import com.tgfcodes.upfile.domain.upload.UploadInput;
import com.tgfcodes.upfile.domain.upload.UploadResult;
import com.tgfcodes.upfile.infrastructure.storage.StorageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

@AppService
public class UploadFileUseCase {

    private static final Logger log = LoggerFactory.getLogger(UploadFileUseCase.class);

    private static final String HASH_ALGORITHM = "XXH3";

    private final FileHashCalculator hashCalculator;

    private final FileDeduplicationService deduplicationService;

    private final Storage storage;

    private final StoredFiles storedFiles;

    private final StorageProperties storageProperties;

    public UploadFileUseCase(final FileHashCalculator hashCalculator,
                             final FileDeduplicationService deduplicationService,
                             final Storage storage,
                             final StoredFiles storedFiles,
                             final StorageProperties storageProperties) {
        this.hashCalculator = hashCalculator;
        this.deduplicationService = deduplicationService;
        this.storage = storage;
        this.storedFiles = storedFiles;
        this.storageProperties = storageProperties;
    }

    @AppTransactional
    public UploadFileOutput execute(UploadFileInput command) {

        String hash = calculateHash(command);
        deduplicationService.ensureUniqueness(hash);
        UploadResult storageResult = uploadToStorage(command, hash);

        StoredFile storedFile = StoredFile.create(
                storageResult.id(),
                storageResult.bucket(),
                storageResult.fileName(),
                storageResult.key(),
                storageResult.eTag(),
                storageResult.hash(),
                storageResult.extension(),
                storageResult.contentType(),
                storageResult.contentDisposition(),
                storageResult.size(),
                storageResult.mimeType(),
                storageResult.type()
        );

        try {
            storedFile = storedFiles.save(storedFile);
        } catch (Exception ex) {
            log.error("Failed to persist file metadata", ex);

            storage.delete(storageResult.bucket(), storageResult.key());
            throw new StorageException("Failed to persist file metadata");
        }

        log.info("File '{}' processed and metadata persisted successfully", storedFile.id());
        return UploadFileOutput.from(storedFile);
    }

    private String calculateHash(UploadFileInput command) {
        try (InputStream inputStream = command.streamSupplier().call()) {
            return hashCalculator.calculateHash(inputStream);

        } catch (Exception ex) {
            log.error("Failed to calculate hash", ex);
            throw new StorageException("Failed to read stream for hash calculation");
        }
    }

    private UploadResult uploadToStorage(UploadFileInput command, String hash) {
        try (InputStream inputStream = command.streamSupplier().call()) {
            UploadInput uploadInput = new UploadInput(
                    command.fileName(),
                    hash,
                    HASH_ALGORITHM,
                    command.contentType(),
                    "attachment",
                    command.size(),
                    inputStream
            );
            return storage.store(storageProperties.getBucket(), uploadInput);

        } catch (Exception ex) {
            log.error("Failed to upload file to storage", ex);
            throw new StorageException("Failed to read stream for storage upload");
        }
    }
}