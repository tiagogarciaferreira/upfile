package com.tgfcodes.upfile.infrastructure.security;

import com.tgfcodes.upfile.application.LoginInput;
import com.tgfcodes.upfile.application.LoginOutput;
import com.tgfcodes.upfile.application.LoginUseCase;
import com.tgfcodes.upfile.domain.Checks;
import com.tgfcodes.upfile.domain.exceptions.DomainValidationException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import static java.util.Objects.requireNonNull;

@RequiredArgsConstructor
@NullMarked
@Service
public class LoginUseCaseImpl implements LoginUseCase {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenService jwtTokenService;

    public LoginOutput execute(LoginInput loginInput) {
        Checks.requireNonNull(loginInput, () -> new DomainValidationException("Login input cannot be null"));

        var unauthenticatedToken = new UsernamePasswordAuthenticationToken(loginInput.username(), loginInput.password());
        Authentication authentication = authenticationManager.authenticate(unauthenticatedToken);
        LoginOutput loginOutput = jwtTokenService.generateToken((UserAuth) requireNonNull(authentication.getPrincipal()));

        return new LoginOutput(
                loginOutput.accessToken(),
                loginOutput.type(),
                loginOutput.authType(),
                loginOutput.expiresIn()
        );
    }
}