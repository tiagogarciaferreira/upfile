package com.tgfcodes.upfile.infrastructure.storage;

import com.dynatrace.hash4j.hashing.Hashing;
import com.tgfcodes.upfile.domain.exceptions.InternalServerErrorException;
import com.tgfcodes.upfile.domain.storedfile.FileHashCalculator;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.HexFormat;

@NullMarked
@Component
public final class FileHashCalculatorImpl implements FileHashCalculator {

    private static final Logger log = LoggerFactory.getLogger(FileHashCalculatorImpl.class);

    @Override
    public String calculateHash(InputStream inputStream) {
        try {
            var hasher = Hashing.xxh3_128().hashStream();
            byte[] buffer = new byte[8192];

            try (inputStream) {
                int read;
                while ((read = inputStream.read(buffer)) != -1) {
                    hasher.putBytes(buffer, 0, read);
                }
            }
            return HexFormat.of().formatHex(hasher.get().toByteArray());

        } catch (IOException ex) {
            log.error("I/O error occurred while draining stream for hash calculation", ex);
            throw new InternalServerErrorException("Failed to read file stream for deduplication analysis");
        }
    }
}