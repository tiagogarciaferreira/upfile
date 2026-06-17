package com.tgfcodes.upfile.application.input;

import java.time.Instant;
import java.util.Locale;

import static java.util.Objects.isNull;

public record SearchFilesFilter(
        String fileName,
        String extension,
        String type,
        Instant startDate,
        Instant endDate,
        int pageNumber,
        int pageSize,
        String sort
) {
    public SearchFilesFilter {
        if (pageNumber < 0 || pageNumber > 999) pageNumber = 0;
        if (pageSize <= 0) pageSize = 20;
        if (pageSize > 100) pageSize = 100;
        if (isNull(sort) || sort.isBlank()) sort = "createdAt,desc";

        if (!isNull(fileName)) fileName = fileName.toLowerCase(Locale.US).trim();
        if (!isNull(extension)) extension = extension.toLowerCase(Locale.US).trim();
        if (!isNull(type)) type = type.toLowerCase(Locale.US).trim();
    }
}