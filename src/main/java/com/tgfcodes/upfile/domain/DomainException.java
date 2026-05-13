package com.tgfcodes.upfile.domain;

public abstract class DomainException extends RuntimeException {

    DomainException(String message) {
        super(message, null, true, false);
    }
}
