package com.tgfcodes.upfile.application;

public record LoginOutput(
        String accessToken,
        String type,
        String authType,
        long expiresIn
) {
}