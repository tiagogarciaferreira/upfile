package com.tgfcodes.upfile.domain.exceptions;

public abstract class DomainException extends RuntimeException {

    protected DomainException(String message) {
        super(message, null, true, false);
    }
}
