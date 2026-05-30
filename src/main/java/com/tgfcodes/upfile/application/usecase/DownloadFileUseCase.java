package com.tgfcodes.upfile.application.usecase;

import com.tgfcodes.upfile.application.annotations.AppTransactional;
import com.tgfcodes.upfile.application.output.FileDownloadOutput;
import com.tgfcodes.upfile.domain.Checks;
import com.tgfcodes.upfile.domain.annotations.AppService;
import com.tgfcodes.upfile.domain.storedfile.StoredFile;
import com.tgfcodes.upfile.domain.storedfile.StoredFileNotFoundException;
import com.tgfcodes.upfile.domain.storedfile.StoredFiles;
import com.tgfcodes.upfile.domain.upload.Storage;
import com.tgfcodes.upfile.domain.upload.StorageStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@AppService
public class DownloadFileUseCase {

    private static final Logger log = LoggerFactory.getLogger(DownloadFileUseCase.class);

    private final StoredFiles storedFiles;

    private final Storage storage;

    public DownloadFileUseCase(final StoredFiles storedFiles, final Storage storage) {
        this.storedFiles = storedFiles;
        this.storage = storage;
    }

    @AppTransactional(readOnly = true)
    public FileDownloadOutput execute(UUID fileId) {
        Checks.requireNonNull(fileId, () -> new IllegalArgumentException("File id cannot be null"));

        // 1. Busca metadados na Fonte Única da Verdade (Banco de Dados)
        StoredFile storedFile = storedFiles.findById(fileId).orElseThrow(() -> new StoredFileNotFoundException(fileId));

        // 2. Abre a conexão com o S3 para iniciar o fluxo de bytes
        StorageStream storageStream = storage.download(storedFile.bucket(), storedFile.key());

        // 3. Devolve os dados combinados para a camada web
        FileDownloadOutput fileDownloadOutput = new FileDownloadOutput(
                storedFile.fileName(),
                storedFile.contentType(),
                storedFile.contentDisposition(),
                storedFile.size(),
                storageStream.stream()
        );

        log.info("File {} downloaded", fileId);
        return fileDownloadOutput;
    }

}