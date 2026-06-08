package com.tgfcodes.upfile.application.query;

import java.util.List;

public record PageResultOutput<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
}
