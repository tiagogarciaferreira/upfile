package com.tgfcodes.upfile.presentation;

import com.tgfcodes.upfile.domain.exceptions.DomainValidationException;
import com.tgfcodes.upfile.domain.exceptions.InternalServerErrorException;
import com.tgfcodes.upfile.domain.storedfile.DuplicateFileException;
import com.tgfcodes.upfile.domain.storedfile.StoredFileNotFoundException;
import com.tgfcodes.upfile.domain.upload.BucketNotFoundException;
import com.tgfcodes.upfile.domain.upload.FileNotFoundException;
import com.tgfcodes.upfile.domain.upload.FileTypeMismatchException;
import com.tgfcodes.upfile.domain.upload.StorageException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;

@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Environment environment;

    @ExceptionHandler(DomainValidationException.class)
    public ResponseEntity<ProblemDetail> handleDomainValidation(DomainValidationException ex, HttpServletRequest request) {
        return build(
                HttpStatus.BAD_REQUEST,
                "Validation error",
                ex.getMessage(),
                "/validation-error",
                request
        );
    }

    @ExceptionHandler({BucketNotFoundException.class, FileNotFoundException.class, StoredFileNotFoundException.class})
    public ResponseEntity<ProblemDetail> handleNotFound(RuntimeException ex, HttpServletRequest request) {
        return build(
                HttpStatus.NOT_FOUND,
                "Resource not found",
                ex.getMessage(),
                "/not-found",
                request
        );
    }

    @ExceptionHandler(FileTypeMismatchException.class)
    public ResponseEntity<ProblemDetail> handleFileTypeMismatch(FileTypeMismatchException ex, HttpServletRequest request) {
        return build(
                HttpStatus.BAD_REQUEST,
                "File type mismatch",
                ex.getMessage(),
                "/file-type-mismatch",
                request
        );
    }

    @ExceptionHandler(DuplicateFileException.class)
    public ResponseEntity<ProblemDetail> handleDuplicate(DuplicateFileException ex, HttpServletRequest request) {
        return build(
                HttpStatus.CONFLICT,
                "Duplicate resource",
                ex.getMessage(),
                "/conflict",
                request
        );
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<ProblemDetail> handleStorage(HttpServletRequest request) {
        return build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Storage error",
                "Storage operation failed",
                "/storage-error",
                request
        );
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ProblemDetail> handleInternal(HttpServletRequest request) {
        return build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal server error",
                "Unexpected error occurred",
                "/internal-error",
                request
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGeneric(HttpServletRequest request) {
        return build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal server error",
                "Unexpected error occurred",
                "/internal-error",
                request
        );
    }

    private ResponseEntity<ProblemDetail> build(
            HttpStatus status,
            String title,
            String detail,
            String typePath,
            HttpServletRequest request) {

        String hostName = environment.getRequiredProperty("HOSTNAME", String.class);

        ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setTitle(title);
        problemDetail.setDetail(detail);
        problemDetail.setType(URI.create(hostName + typePath));
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("traceId", "");
        return ResponseEntity.status(status).body(problemDetail);
    }
}