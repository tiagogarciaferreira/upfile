package com.tgfcodes.upfile.application;

import com.tgfcodes.upfile.domain.Checks;
import com.tgfcodes.upfile.domain.exceptions.DomainValidationException;

import static java.util.Arrays.stream;

public enum SortOption {

    TYPE_ASC("type,asc", "type,asc"),
    TYPE_DESC("type,desc", "type,desc"),
    CREATED_AT_ASC("createdAt,asc", "created_at,asc"),
    CREATED_AT_DESC("createdAt,desc", "created_at,desc");

    private final String key;

    private final String value;

    SortOption(final String key, final String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static SortOption from(String rawKey) {
        Checks.requireNonEmpty(rawKey, () -> new DomainValidationException("Sort option cannot be empty"));

        return stream(SortOption.values())
                .filter(option -> option.getKey().equalsIgnoreCase(rawKey))
                .findFirst()
                .orElseThrow(() -> new DomainValidationException("Invalid sort option"));
    }

    public String getField() {
        return this.getValue().substring(0, this.getValue().indexOf(",")).trim();
    }

    public String getDirection() {
        return this.getValue().substring(this.getValue().indexOf(",")).trim();
    }
}