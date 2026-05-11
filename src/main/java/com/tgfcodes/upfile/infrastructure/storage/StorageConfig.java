package com.tgfcodes.upfile.infrastructure.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.net.URI;
import java.nio.file.Paths;

@RequiredArgsConstructor
@Configuration
public class StorageConfig {

    private final StorageProperties storageProperties;

    @Bean
    public S3Client s3Client() {
        final AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(
                storageProperties.getAccessKey(),
                storageProperties.getSecretKey()
        );
        return S3Client.builder()
                .endpointOverride(URI.create(storageProperties.getEndpoint()))
                .region(Region.of(storageProperties.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
                .forcePathStyle(true)
                .build();
    }

    @Bean("testUpload")
    public S3Client testUpload(S3Client s3Client) {

        ListBucketsResponse listBucketsResponse = s3Client.listBuckets();

        PutObjectResponse putObjectResponse = s3Client.putObject(
                PutObjectRequest.builder().bucket(storageProperties.getBucket()).key("doc/br/Rustfs - Create user.png").build(),
                Paths.get("./doc/Rustfs - Create user.png")
        );

        System.out.println("Uploaded Rustfs - Create user.png");

        // 4. Download file
        ResponseInputStream<GetObjectResponse> object = s3Client.getObject(
                GetObjectRequest.builder().bucket(storageProperties.getBucket()).key("doc/br/Rustfs - Create user.png").build()
        );
        
        System.out.println("Downloaded hello.txt");

        return s3Client;
    }
}