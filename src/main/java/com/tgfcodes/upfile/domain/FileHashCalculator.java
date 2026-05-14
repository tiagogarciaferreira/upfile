package com.tgfcodes.upfile.domain;

import java.io.InputStream;

public interface FileHashCalculator {

    String calculateHash(InputStream inputStream);
}
