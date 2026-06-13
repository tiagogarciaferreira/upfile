package com.tgfcodes.upfile.presentation.file;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Tag(name = "Files", description = "Endpoints for uploading, downloading, and managing system files")
public interface FilesApi {

    @Operation(summary = "Upload a single file", description = "Receives a multipart file, validates content limits, calculates hash and metadata asynchronously, and persists into storage.")
    @ApiResponse(responseCode = "201", description = "File successfully uploaded and indexed.", content = @Content(schema = @Schema(implementation = UploadFileResponse.class)))
    @ApiResponse(responseCode = "400", description = "Invalid payload format constraints", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "401", description = "Missing, expired or invalid JWT access token", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "403", description = "Account lacks required structural roles or write privileges", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "File resource identifier not found", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "413", description = "Payload size exceeded the maximum configured limit allowed by system(10MB)", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "500", description = "Storage transmission or parsing failure", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    ResponseEntity<UploadFileResponse> upload(@RequestBody(description = "The binary multipart file payload", required = true) MultipartFile file);

    @Operation(summary = "Get file details", description = "Retrieves complete structured metadata belonging to a file tracking record.")
    @ApiResponse(responseCode = "200", description = "Metadata retrieved successfully.", content = @Content(schema = @Schema(implementation = FileDetailsResponse.class)))
    @ApiResponse(responseCode = "400", description = "Provided ID is malformed or violates UUID syntax constraints", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "401", description = "Missing, expired or invalid JWT access token", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "403", description = "Account lacks read capabilities for this resource domain", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "File resource identifier not found", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "500", description = "An unexpected internal server error occurred while processing the request", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    ResponseEntity<FileDetailsResponse> details(@Parameter(description = "The unique file UUID", required = true, in = ParameterIn.PATH) UUID id);

    @Operation(summary = "Delete file track", description = "Removes file references and coordinates atomic storage eviction.")
    @ApiResponse(responseCode = "204", description = "File successfully evicted from ecosystem")
    @ApiResponse(responseCode = "400", description = "Provided ID is malformed or violates UUID syntax constraints", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "401", description = "Missing, expired or invalid JWT access token", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "403", description = "Account lacks administrative clearance to execute deletions", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Resource reference targeting failure", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "500", description = "An unexpected internal server error occurred while processing the request", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    ResponseEntity<Void> delete(@Parameter(description = "The unique file UUID", required = true, in = ParameterIn.PATH) UUID id);

    @Operation(summary = "Search dynamic files index", description = "Paged lookup engine against transactional record indexes utilizing filter conditions.")
    @ApiResponse(responseCode = "200", description = "Filtered match collection payload generated.")
    @ApiResponse(responseCode = "401", description = "Missing, expired or invalid JWT access token", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "403", description = "Account lacks query execution clearance", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "400", description = "Invalid query filter parameters or pagination indices", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "500", description = "An unexpected internal server error occurred while processing the request", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    ResponseEntity<PageResponse<FileMetadata>> search(
            SearchFileRequest searchFileRequest,
            @Parameter(description = "Page cursor index", schema = @Schema(defaultValue = "0")) int pageNumber,
            @Parameter(description = "Page chunk size slice window", schema = @Schema(defaultValue = "10")) int pageSize,
            @Parameter(description = "Dynamic sort string (field,direction)", schema = @Schema(defaultValue = "type,asc")) String sort
    );
}
