package com.tgfcodes.upfile.presentation.file;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Standard paginated envelope container for collection responses")
public record PageResponse<T>(

        @Schema(description = "Array containing the chunk data payload filtered by the query execution context")
        List<T> content,

        @Schema(description = "Current page index cursor (0-based indexing)", example = "0")
        int page,

        @Schema(description = "Requested page slice chunk capacity limit sizing", example = "10")
        int size,

        @Schema(description = "Total structural record matching units identified within storage layer", example = "145")
        long totalItems,

        @Schema(description = "Total page iterations mathematically computed against current slice capacity", example = "15")
        int totalPages
) {
}