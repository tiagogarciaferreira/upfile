package com.tgfcodes.upfile.infrastructure.storage;

import com.tgfcodes.upfile.domain.exceptions.InternalServerErrorException;
import com.tgfcodes.upfile.domain.storedfile.FileHashCalculator;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(FileHashCalculatorImpl.class);

    private static final String ALGORITHM = "XXHASH3";

    @Override
    public String calculateHash(InputStream inputStream) {
        try {
            var digest = MessageDigest.getInstance(ALGORITHM);

            try (var digestStream = new DigestInputStream(inputStream, digest)) {
                digestStream.transferTo(OutputStream.nullOutputStream());
            }
            return HexFormat.of().formatHex(digest.digest());

        } catch (NoSuchAlgorithmException ex) {
            log.error("Security provider required for hash algorithm is missing [algorithm={}]", ALGORITHM, ex);
            throw new InternalServerErrorException("System configured with invalid hash algorithm");

        } catch (IOException ex) {
            log.error("I/O error occurred while draining stream for hash calculation", ex);
            throw new InternalServerErrorException("Failed to read file stream for deduplication analysis");
        }
    }
}