package com.tgfcodes.upfile.infrastructure.persistence;

import com.tgfcodes.upfile.domain.StoredFile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StoredFileMapper {

    StoredFile toDomain(StoredFileEntity storedFileEntity);

    StoredFileEntity toEntity(StoredFile storedFile);
}