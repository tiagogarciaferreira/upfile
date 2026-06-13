package com.tgfcodes.upfile.presentation.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

@Tag(name = "Authentication", description = "Identity and access management")
public interface AuthApi {

    @Operation(
            summary = "Authenticate user",
            description = "Authenticates a user by username and password and returns a JWT access token."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successfully authenticated. Returns the JWT token.",
            content = @Content(schema = @Schema(implementation = LoginResponse.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Validation errors on the login payload",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class))
    )
    @ApiResponse(
            responseCode = "500",
            description = "An unexpected internal server error occurred while processing the request",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class))
    )
    ResponseEntity<LoginResponse> login(
            @RequestBody(description = "User credentials payload required for authentication", required = true)
            LoginRequest loginRequest
    );
}