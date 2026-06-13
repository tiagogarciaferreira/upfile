package com.tgfcodes.upfile.presentation.auth;

import com.tgfcodes.upfile.application.input.LoginInput;
import com.tgfcodes.upfile.application.output.LoginOutput;
import com.tgfcodes.upfile.application.usecase.LoginUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/auth", version = "1.0")
public class AuthController implements AuthApi {

    private final LoginUseCase loginUseCase;

    @Override
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginOutput loginOutput = loginUseCase.execute(new LoginInput(loginRequest.username(), loginRequest.password()));
        LoginResponse loginResponse = LoginResponse.from(loginOutput);
        log.info("User logged in: {}", loginRequest);
        return ResponseEntity.ok().body(loginResponse);
    }
}