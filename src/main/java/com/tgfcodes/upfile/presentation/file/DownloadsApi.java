package com.tgfcodes.upfile.presentation.file;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Downloads", description = "Endpoints for high-throughput binary file streaming")
public interface DownloadsApi {

    @Operation(summary = "Download file binary", description = "Streams high-throughput data back down over pipeline attachments through InputStream chunks.")
    @ApiResponse(responseCode = "200", description = "Binary octet stream attachment response.", content = @Content(schema = @Schema(type = "string", format = "binary")))
    @ApiResponse(responseCode = "400", description = "Provided ID is malformed or violates UUID syntax constraints", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "401", description = "Missing, expired or invalid JWT access token", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "403", description = "Account lacks download clearance for this scope", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Target identifier lookup failure", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "500", description = "An unexpected internal server error occurred while processing the request", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    ResponseEntity<Resource> download(@Parameter(description = "The unique file UUID to stream down", required = true, in = ParameterIn.PATH) UUID id);
}
