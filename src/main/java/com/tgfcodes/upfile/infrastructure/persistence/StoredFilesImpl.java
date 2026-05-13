package com.tgfcodes.upfile.infrastructure.persistence;

import com.tgfcodes.upfile.domain.StoredFile;
import com.tgfcodes.upfile.domain.StoredFiles;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@RequiredArgsConstructor
@Component
@Transactional(readOnly = true)
public class StoredFilesImpl implements StoredFiles {

    private final StoredFileRepository storedFileRepository;

    @Override
    public StoredFile save(StoredFile storedFile) {
        return null;
    }

    @Override
    public StoredFile findById(UUID id) {
        return null;
    }

    @Override
    public StoredFile findByHash(String hash) {
        return null;
    }

    @Override
    public void deleteById(UUID id) {

    }
}
