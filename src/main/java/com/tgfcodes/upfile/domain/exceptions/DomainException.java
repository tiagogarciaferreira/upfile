package com.tgfcodes.upfile.domain.exceptions;

public abstract class DomainException extends RuntimeException {

    DomainException(String message) {
        super(message, null, true, false);
    }
}
