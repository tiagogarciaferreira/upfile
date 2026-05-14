package com.tgfcodes.upfile.infrastructure.persistence;

import com.tgfcodes.upfile.domain.StoredFile;
import com.tgfcodes.upfile.domain.StoredFileNotFoundException;
import com.tgfcodes.upfile.domain.StoredFiles;
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
    public Optional<StoredFile> findByHash(String hash) {
        return storedFileRepository.findByHash(hash).map(storedFileMapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        boolean exists = storedFileRepository.existsById(id);
        if (!exists) throw new StoredFileNotFoundException(id);
        storedFileRepository.deleteById(id);
    }
}
