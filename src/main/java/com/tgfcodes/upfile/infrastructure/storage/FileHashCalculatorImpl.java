package com.tgfcodes.upfile.infrastructure.storage;

import com.tgfcodes.upfile.domain.FileHashCalculator;
import lombok.SneakyThrows;
import org.jspecify.annotations.NullMarked;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@NullMarked
@Component
public final class FileHashCalculatorImpl implements FileHashCalculator {

    private static final String ALGORITHM = "SHA-256";

    @SneakyThrows({NoSuchAlgorithmException.class, IOException.class})
    @Override
    public String calculateHash(InputStream inputStream) {
        var digest = MessageDigest.getInstance(ALGORITHM);

        try (var digestStream = new DigestInputStream(inputStream, digest)) {
            digestStream.transferTo(OutputStream.nullOutputStream());
        }
        return HexFormat.of().formatHex(digest.digest());
    }
}