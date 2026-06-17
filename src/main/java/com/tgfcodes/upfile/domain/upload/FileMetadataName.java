package com.tgfcodes.upfile.domain.upload;

public enum FileMetadataName {

    ORIGINAL_FILENAME("originalFilename"),
    ENTITY_ID("entityId"),
    ENTITY_TYPE("entityType"),
    MEDIA_TYPE("mediaType"),
    FILE_EXTENSION("fileExtension"),
    UPLOADED_AT("uploadedAt"),
    UPLOADED_BY("uploadedBy");

    private final String value;

    FileMetadataName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
