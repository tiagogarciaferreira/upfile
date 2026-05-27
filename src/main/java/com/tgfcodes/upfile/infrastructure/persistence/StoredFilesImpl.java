package com.tgfcodes.upfile.infrastructure.persistence;

import com.tgfcodes.upfile.domain.storedfile.StoredFile;
import com.tgfcodes.upfile.domain.storedfile.StoredFiles;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component
@Transactional(readOnly = true)
public class StoredFilesImpl implements StoredFiles {

    private final StoredFileRepository storedFileRepository;

    private final StoredFileMapper storedFileMapper;

    @Override
    public StoredFile save(StoredFile storedFile) {
        StoredFileEntity storedFileEntity = storedFileMapper.toEntity(storedFile);
        storedFileEntity = storedFileRepository.save(storedFileEntity);
        return storedFileMapper.toDomain(storedFileEntity);
    }

    @Override
    public Optional<StoredFile> findById(UUID id) {
        return storedFileRepository.findById(id).map(storedFileMapper::toDomain);
    }

    @Override
    public boolean existsByHash(String hash) {
        return storedFileRepository.existsByHash(hash);
    }

    @Override
    public void deleteById(UUID id) {
        storedFileRepository.deleteById(id);
    }
}
