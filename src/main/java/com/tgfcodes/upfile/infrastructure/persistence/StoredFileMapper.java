package com.tgfcodes.upfile.infrastructure.persistence;

import com.tgfcodes.upfile.domain.storedfile.StoredFile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface StoredFileMapper {

    @Mapping(target = "eTag", source = "ETag")
    StoredFile toDomain(StoredFileEntity storedFileEntity);

    @Mapping(target = "createdByUserId", ignore = true)
    StoredFileEntity toEntity(StoredFile storedFile);
}