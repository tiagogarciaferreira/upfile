package com.tgfcodes.upfile.presentation.file;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.Instant;

import static java.util.Objects.isNull;

public record SearchFileRequest(

        @Size(max = 255, message = "fileName must not exceed 255 characters.")
        @Pattern(
                regexp = "^[a-zA-Z0-9._-]+$",
                message = "fileName contains invalid characters. Allowed: letters, digits, dot, underscore, hyphen."
        )
        String fileName,

        @Size(max = 10, message = "extension must not exceed 10 characters.")
        @Pattern(
                regexp = "^[a-zA-Z]+$",
                message = "extension must contain only letters."
        )
        String extension,

        @Size(max = 20, message = "type must not exceed 20 characters.")
        @Pattern(
                regexp = "^[a-zA-Z0-9_-]+$",
                message = "type contains invalid characters. Allowed: letters, digits, underscore, hyphen."
        )
        String type,

        @PastOrPresent(message = "startDate must be in the past or present.")
        Instant startDate,

        @PastOrPresent(message = "endDate must be in the past or present.")
        Instant endDate

) {

    private static final Instant MIN_DATE = Instant.parse("2000-01-01T00:00:00Z");

    @AssertTrue(message = "startDate must be before or equal to endDate.")
    public boolean isDateRangeValid() {
        if (isNull(startDate) || isNull(endDate)) return true;
        return !startDate.isAfter(endDate);
    }

    @AssertTrue(message = "startDate must not be before 2000-01-01T00:00:00Z.")
    public boolean isStartDateAfterMin() {
        if (isNull(startDate)) return true;
        return !startDate.isBefore(MIN_DATE);
    }

    @AssertTrue(message = "endDate must not be before 2000-01-01T00:00:00Z.")
    public boolean isEndDateAfterMin() {
        if (isNull(endDate)) return true;
        return !endDate.isBefore(MIN_DATE);
    }
}