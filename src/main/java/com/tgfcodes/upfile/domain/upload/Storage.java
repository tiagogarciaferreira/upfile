package com.tgfcodes.upfile.domain.upload;

public interface Storage {

    UploadResult store(String bucket, UploadInput uploadInput);

    UploadRetrieve retrieve(String bucket, String key);

    StorageStream download(String bucket, String key);

    void delete(String bucket, String key);

    boolean existsBucket(String bucket);

    boolean existsFile(String bucket, String key);
}