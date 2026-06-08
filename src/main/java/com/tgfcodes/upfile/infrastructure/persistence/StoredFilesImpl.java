package com.tgfcodes.upfile.infrastructure.persistence;

import com.tgfcodes.upfile.domain.storedfile.PageResult;
import com.tgfcodes.upfile.domain.storedfile.StoredFile;
import com.tgfcodes.upfile.domain.storedfile.StoredFileFilter;
import com.tgfcodes.upfile.domain.storedfile.StoredFiles;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
        storedFileEntity.setCreatedByUserId(UUID.fromString("cab2ae93-e295-48da-b0b3-5958cd6e0434"));
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

    @Override
    public PageResult<StoredFile> search(StoredFileFilter storedFileFilter) {

        Specification<StoredFileEntity> storedFileEntitySpecification = StoredFileSpecification.from(storedFileFilter);
        StoredFileFilter.Sort sortFilter = storedFileFilter.sort();
        StoredFileFilter.Page pageFilter = storedFileFilter.page();

        Page<StoredFileEntity> page = storedFileRepository.findAll(
                storedFileEntitySpecification,
                PageRequest.of(pageFilter.pageNumber(), pageFilter.pageSize(),
                        Sort.by(Sort.Direction.fromString(sortFilter.direction()), sortFilter.field()))
        );

        return new PageResult<>(
                page.getContent().stream().map(storedFileMapper::toDomain).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
