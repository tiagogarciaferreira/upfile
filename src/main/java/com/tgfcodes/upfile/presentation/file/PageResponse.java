package com.tgfcodes.upfile.presentation.file;

import java.util.List;

public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalItems,
        int totalPages
) {
}