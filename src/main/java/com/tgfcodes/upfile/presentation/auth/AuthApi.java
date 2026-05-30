package com.tgfcodes.upfile.presentation.auth;

import com.tgfcodes.upfile.application.output.LoginOutput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Authentication", description = "Identity and access management")
public interface AuthApi {

    @Operation(
            summary = "Authenticate user",
            description = "Authenticates a user by username and password and returns a JWT access token."
    )
    @ApiResponse(responseCode = "200", description = "Successfully authenticated. Returns the JWT token.")
    @ApiResponse(responseCode = "400", description = "Validation errors on the login payload", content = @Content)
    @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content)
    ResponseEntity<LoginOutput> login(
            @Parameter(description = "User credentials payload", required = true) LoginRequest loginRequest
    );
}