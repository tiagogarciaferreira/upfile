package com.tgfcodes.upfile.infrastructure.storage;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "storage")
public class StorageProperties {

    private String bucket;

    private String region;

    private String endpoint;

    private String accessKey;

    private String secretKey;
}