package com.tgfcodes.upfile.infrastructure.storage;

import com.tgfcodes.upfile.domain.upload.Storage;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@NullMarked
@RequiredArgsConstructor
@Profile("local")
@Component
public class S3BucketObjectsCleanupOnStartup implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(S3BucketObjectsCleanupOnStartup.class);

    private final StorageProperties storageProperties;

    private final Storage storage;

    @Override
    public void run(ApplicationArguments args) {

        String bucket = storageProperties.getBucket();

        if (storage.existsBucket(bucket)) {
            log.info("Clearing bucket {} before starting the application", bucket);
            storage.clearBucket(bucket);
            log.info("Bucket {} cleared", bucket);
        }
    }
}