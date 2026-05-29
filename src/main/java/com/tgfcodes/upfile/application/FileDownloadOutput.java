package com.tgfcodes.upfile.application;

import java.io.InputStream;

public record FileDownloadOutput(
        String fileName,
        String contentType,
        String contentDisposition,
        long size,
        InputStream stream
) {

}
