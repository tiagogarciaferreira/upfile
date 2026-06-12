package com.tgfcodes.upfile.presentation;

import com.tgfcodes.upfile.domain.exceptions.DomainValidationException;
import com.tgfcodes.upfile.domain.exceptions.InternalServerErrorException;
import com.tgfcodes.upfile.domain.storedfile.DuplicateFileException;
import com.tgfcodes.upfile.domain.storedfile.StoredFileNotFoundException;
import com.tgfcodes.upfile.domain.upload.BucketNotFoundException;
import com.tgfcodes.upfile.domain.upload.FileNotFoundException;
import com.tgfcodes.upfile.domain.upload.FileTypeMismatchException;
import com.tgfcodes.upfile.domain.upload.StorageException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.net.URI;
import java.time.Instant;
import java.util.*;

import static com.tgfcodes.upfile.presentation.ExceptionKeys.*;
import static java.util.Objects.isNull;

@RequiredArgsConstructor
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    private static final String UNKNOWN = "unknown";

    private final Environment environment;

    @ExceptionHandler(DomainValidationException.class)
    public ResponseEntity<ProblemDetail> handleDomainValidation(DomainValidationException ex, WebRequest request) {
        return build(
                HttpStatus.BAD_REQUEST,
                "Validation Error",
                ex.getMessage(),
                "/errors/validation",
                new HashMap<>(),
                request
        );
    }

    @ExceptionHandler({BucketNotFoundException.class, FileNotFoundException.class, StoredFileNotFoundException.class})
    public ResponseEntity<ProblemDetail> handleNotFoundException(RuntimeException ex, WebRequest request) {
        return build(
                HttpStatus.NOT_FOUND,
                "Resource Not Found",
                ex.getMessage(),
                "/errors/not-found",
                new HashMap<>(),
                request
        );
    }

    @ExceptionHandler(FileTypeMismatchException.class)
    public ResponseEntity<ProblemDetail> handleFileTypeMismatchException(FileTypeMismatchException ex, WebRequest request) {
        return build(
                HttpStatus.BAD_REQUEST,
                "Unsupported File Type",
                ex.getMessage(),
                "/errors/file-type-mismatch",
                new HashMap<>(),
                request
        );
    }

    @ExceptionHandler(DuplicateFileException.class)
    public ResponseEntity<ProblemDetail> handleDuplicateFileException(DuplicateFileException ex, WebRequest request) {
        return build(
                HttpStatus.CONFLICT,
                "Resource Conflict",
                ex.getMessage(),
                "/errors/conflict",
                new HashMap<>(),
                request
        );
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<ProblemDetail> handleStorageException(WebRequest request) {
        return build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Storage Operation Failed",
                "An unexpected error occurred during the storage operation.",
                "/errors/storage-failure",
                new HashMap<>(),
                request
        );
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ProblemDetail> handleInternalServerErrorException(WebRequest request) {
        return build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                "An unexpected error occurred while processing the request.",
                "/errors/internal-server-error",
                new HashMap<>(),
                request
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
                                                                               WebRequest request) {

        final Set<Map<String, Object>> properties = new HashSet<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {

            String message = error.isBindingFailure() ? "Invalid value" : error.getDefaultMessage();
            properties.add(Map.of(
                    FIELD_NAME, error.getField(),
                    FIELD_VALUE, isNull(error.getRejectedValue()) ? UNKNOWN : error.getRejectedValue(),
                    MESSAGE, isNull(message) ? UNKNOWN : message
            ));
        });

        return build(
                HttpStatus.BAD_REQUEST,
                "Invalid Request Parameters",
                "One or more fields have invalid values.",
                "/errors/invalid-parameters",
                new HashMap<>(Map.of(ERRORS, properties)),
                request
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleHttpMessageNotReadableException(WebRequest request) {
        return build(
                HttpStatus.BAD_REQUEST,
                "Malformed Request",
                "The request body is unreadable or malformed.",
                "/errors/malformed-request",
                new HashMap<>(),
                request
        );
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ProblemDetail> handleMaxUploadSizeExceededException(WebRequest request) {
        return build(
                HttpStatus.CONTENT_TOO_LARGE,
                "Payload Too Large",
                "The uploaded file exceeds the maximum allowed size.",
                "/errors/payload-too-large",
                new HashMap<>(),
                request
        );
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ProblemDetail> handleNoResourceFoundException(WebRequest request) {
        return build(
                HttpStatus.NOT_FOUND,
                "Endpoint Not Found",
                "The requested endpoint does not exist.",
                "/errors/endpoint-not-found",
                new HashMap<>(),
                request
        );
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ProblemDetail> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex,
                                                                                  WebRequest request) {
        return build(
                HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                "Unsupported Media Type",
                "Content-Type '%s' is not supported.".formatted(isNull(ex.getContentType()) ? UNKNOWN : ex.getContentType()),
                "/errors/unsupported-media-type",
                new HashMap<>(),
                request
        );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ProblemDetail> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex, WebRequest request) {
        return build(
                HttpStatus.METHOD_NOT_ALLOWED,
                "Method Not Allowed",
                ex.getMessage(),
                "/errors/method-not-allowed",
                new HashMap<>(),
                request
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex,
                                                                                   WebRequest request) {
        Class<?> requiredType = ex.getRequiredType();
        Map<String, Object> properties = Map.of(
                FIELD_NAME, ex.getName(),
                FIELD_VALUE, isNull(ex.getValue()) ? UNKNOWN : ex.getValue(),
                MESSAGE, "The value required type '%s'".formatted(isNull(requiredType) ? UNKNOWN : requiredType.getName())
        );

        return build(
                HttpStatus.BAD_REQUEST,
                "Type Mismatch",
                "A request parameter has an invalid type.",
                "/errors/type-mismatch",
                new HashMap<>(Map.of("errors", List.of(properties))),
                request
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentTypeMismatchException(ConstraintViolationException ex,
                                                                                   WebRequest request) {

        final Set<Map<String, Object>> properties = new HashSet<>();
        ex.getConstraintViolations().forEach(violation -> {

            Path propertyPath = violation.getPropertyPath();
            Path.Node lastNode = null;

            for (Path.Node node : propertyPath) {
                lastNode = node;
            }
            properties.add(Map.of(
                    FIELD_NAME, !isNull(lastNode) ? lastNode.getName() : "unknow",
                    FIELD_VALUE, !isNull(violation.getInvalidValue()) ? violation.getInvalidValue() : "unknow",
                    MESSAGE, violation.getMessage()
            ));
        });

        return build(
                HttpStatus.BAD_REQUEST,
                "Constraint Violation",
                "One or more constraints were violated.",
                "/errors/constraint-violation",
                new HashMap<>(Map.of(ERRORS, properties)),
                request
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleException(WebRequest request) {
        return build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                "An unexpected error occurred.",
                "/errors/internal-server-error",
                new HashMap<>(),
                request
        );
    }

    private ResponseEntity<ProblemDetail> build(
            HttpStatus status,
            String title,
            String detail,
            String typePath,
            Map<String, Object> properties,
            WebRequest request) {

        String hostName = environment.getRequiredProperty("HOSTNAME", String.class);
        String requestURI = ((ServletWebRequest) request).getRequest().getRequestURI();
        properties.putIfAbsent(TIMESTAMP, Instant.now());

        ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setTitle(title);
        problemDetail.setDetail(detail);
        problemDetail.setType(URI.create(hostName + typePath));
        problemDetail.setInstance(URI.create(requestURI));
        problemDetail.setProperties(properties);
        return ResponseEntity.status(status).body(problemDetail);
    }
}