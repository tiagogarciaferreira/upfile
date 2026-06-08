package com.tgfcodes.upfile.domain.storedfile;

import com.tgfcodes.upfile.domain.Checks;
import com.tgfcodes.upfile.domain.exceptions.DomainValidationException;

import java.time.Instant;

import static java.util.Objects.isNull;

public record StoredFileFilter(
        String fileName,
        String extension,
        String type,
        Instant startDate,
        Instant endDate,
        Page page,
        Sort sort
) {
    public StoredFileFilter {
        Checks.requireNonNull(page, () -> new DomainValidationException("Page cannot be null"));
        Checks.requireNonNull(sort, () -> new DomainValidationException("Sort cannot be null"));
    }

    public record Page(int pageNumber, int pageSize) {
        public Page {
            if (pageNumber < 0 || pageNumber > 999) pageNumber = 0;
            if (pageSize <= 0) pageSize = 20;
            if (pageSize > 100) pageSize = 100;
        }
    }

    public record Sort(String field, String direction) {
        public Sort {
            if (isNull(field) || field.isBlank()) field = "type";
            if (isNull(direction) || direction.isBlank()) direction = "asc";
        }
    }
}