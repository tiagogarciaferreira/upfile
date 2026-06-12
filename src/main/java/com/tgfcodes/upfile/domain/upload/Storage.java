package com.tgfcodes.upfile.domain.upload;

public interface Storage {

    UploadResult store(String bucket, UploadInput uploadInput);

    StorageStream download(String bucket, String key);

    void clearBucket(String bucket);

    void delete(String bucket, String key);

    boolean existsBucket(String bucket);

    boolean existsFile(String bucket, String key);
}