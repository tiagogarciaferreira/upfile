package com.tgfcodes.upfile.domain.storedfile;

import java.io.InputStream;

public interface FileHashCalculator {

    String calculateHash(InputStream inputStream);
}
