package com.tgfcodes.upfile.infrastructure.storage;

import com.tgfcodes.upfile.domain.storedfile.StoredFile;
import com.tgfcodes.upfile.domain.upload.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.tika.Tika;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@NullMarked
@Component
public class StorageImpl implements Storage {

    private static final Logger log = LoggerFactory.getLogger(StorageImpl.class);

    private static final int TIKA_MARK_LIMIT_BYTES = 8192;

    private final S3Client s3Client;

    private final Tika tika;

    private final UploadResultMapper uploadResultMapper;

    private final UploadRetrieveMapper uploadRetrieveMapper;

    @SneakyThrows
    @Override
    public UploadResult store(String bucket, UploadInput uploadInput) {
        checkBucketExists(bucket);

        String fileName = uploadInput.fileName().toLowerCase().trim();
        String extension = getFileExtension(fileName);
        String mimeType;

        InputStream safeStream = ensureMarkSupported(uploadInput.inputStream());
        try {
            safeStream.mark(TIKA_MARK_LIMIT_BYTES);
            mimeType = tika.detect(safeStream).trim();
            safeStream.reset();

        } catch (IOException ex) {
            log.error("Failed to read stream for mimetype detection", ex);
            throw new StorageException("Failed to read stream for mimetype detection");
        }

        String type = extractMediaType(mimeType);
        String mimeTypeExtension = extractMimeExtension(mimeType);

        if (!extension.equals(mimeTypeExtension)) {
            log.error("File extension '{}' does not match with mime type '{}'", extension, mimeType);
            throw new FileTypeMismatchException("File extension does not match with mime type");
        }

        Map<String, String> metadata = createMetadata(uploadInput, mimeType, type, extension);
        String key = String.format("%s/%s/%s/%s.%s",
                type,
                StoredFile.class.getSimpleName().toLowerCase(),
                metadata.get("entityId"),
                UUID.randomUUID(),
                extension);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(uploadInput.contentType())
                .contentLength(uploadInput.size())
                .contentDisposition(uploadInput.contentDisposition())
                .metadata(metadata)
                .checksumAlgorithm(uploadInput.algorithm())
                .checksumXXHASH3(uploadInput.hash())
                .build();

        try {
            RequestBody requestBody = RequestBody.fromInputStream(safeStream, uploadInput.size());
            PutObjectResponse putObjectResponse = s3Client.putObject(putObjectRequest, requestBody);

            log.info("File successfully uploaded to S3");
            return uploadResultMapper.toUploadResult(putObjectRequest, putObjectResponse.eTag().replace("\"", ""));

        } catch (S3Exception ex) {
            log.error("S3 Provider rejected the upload request", ex);
            throw new StorageException("Failed to upload file");

        } finally {
            safeStream.close();
            uploadInput.inputStream().close();
        }
    }

    @Override
    public UploadRetrieve retrieve(String bucket, String key) {
        checkBucketExists(bucket);
        checkFileExists(bucket, key);

        try {
            ResponseInputStream<GetObjectResponse> response = s3Client.getObject(builder -> builder.bucket(bucket).key(key));
            return uploadRetrieveMapper.toUploadRetrieve(response);

        } catch (S3Exception ex) {
            log.error("File '{}' not found in S3 bucket '{}'", key, bucket, ex);
            throw new StorageException("File '%s' not found in bucket '%s'".formatted(bucket, key));
        }
    }

    @Override
    public void delete(String bucket, String key) {
        checkBucketExists(bucket);
        checkFileExists(bucket, key);

        try {
            s3Client.deleteObject(builder -> builder.bucket(bucket).key(key));
            log.info("File '{}' deleted from S3 bucket '{}'", key, bucket);

        } catch (S3Exception ex) {
            log.error("Failed to delete file '{}' from S3 bucket '{}'", key, bucket, ex);
            throw new StorageException("Failed to delete file '%s' from bucket '%s'".formatted(key, bucket));
        }
    }

    @Override
    public boolean existsBucket(String bucketName) {
        try {
            return s3Client.listBuckets()
                    .buckets()
                    .stream()
                    .anyMatch(bucket -> bucket.name().equals(bucketName));
        } catch (S3Exception ex) {
            log.error("Failed to list buckets", ex);
            throw new StorageException("Failed to list buckets");
        }
    }

    @Override
    public boolean existsFile(String bucket, String key) {
        try {
            return s3Client.listObjects(builder -> builder.bucket(bucket).prefix(key))
                    .contents()
                    .stream()
                    .anyMatch(object -> object.key().equals(key));
        } catch (S3Exception ex) {
            log.error("Failed to list objects in bucket '{}'", bucket, ex);
            throw new StorageException("Failed to list objects in bucket '%s'".formatted(bucket));
        }
    }

    @Override
    public StorageStream download(String bucket, String key) {
        checkBucketExists(bucket);
        checkFileExists(bucket, key);

        try {
            ResponseInputStream<GetObjectResponse> response = s3Client.getObject(builder -> builder.bucket(bucket).key(key));
            return StorageStream.from(response);

        } catch (S3Exception ex) {
            log.error("Failed to download file '{}' from S3 bucket '{}'", key, bucket, ex);
            throw new StorageException("Failed to download file '%s' from bucket '%s'".formatted(key, bucket));
        }
    }

    private void checkBucketExists(String bucket) {
        if (!existsBucket(bucket)) {
            log.error("Bucket '{}' does not exist", bucket);
            throw new BucketNotFoundException(bucket);
        }
    }

    private void checkFileExists(String bucket, String key) {
        if (!existsFile(bucket, key)) {
            log.error("File '{}' does not exist in bucket '{}'", key, bucket);
            throw new FileNotFoundException(key);
        }
    }

    private Map<String, String> createMetadata(UploadInput uploadInput, String mimeType, String type, String extension) {
        return Map.of(
                FileMetadataName.ORIGINAL_FILENAME.getValue(), uploadInput.fileName(),
                FileMetadataName.ENTITY_ID.getValue(), UUID.randomUUID().toString(),
                FileMetadataName.ENTITY_TYPE.getValue(), StoredFile.class.getSimpleName(),
                FileMetadataName.MIME_TYPE.getValue(), mimeType,
                FileMetadataName.MEDIA_TYPE.getValue(), type,
                FileMetadataName.FILE_EXTENSION.getValue(), extension,
                FileMetadataName.UPLOADED_AT.getValue(), Instant.now().toString(),
                FileMetadataName.UPLOADED_BY.getValue(), UUID.randomUUID().toString()
        );
    }

    private String getFileExtension(String fileName) {
        int lastIndexOf = fileName.lastIndexOf(".");
        return lastIndexOf == -1 ? "" : fileName.substring(lastIndexOf + 1);
    }

    private String extractMediaType(String mimeType) {
        int index = mimeType.indexOf("/");
        return index == -1 ? mimeType : mimeType.substring(0, index).toLowerCase();
    }

    private String extractMimeExtension(String mimeType) {
        int index = mimeType.indexOf("/");
        return index == -1 ? "" : mimeType.substring(index + 1).toLowerCase();
    }

    private InputStream ensureMarkSupported(InputStream inputStream) {
        if (inputStream.markSupported()) return inputStream;
        return new BufferedInputStream(inputStream, TIKA_MARK_LIMIT_BYTES);
    }
}
