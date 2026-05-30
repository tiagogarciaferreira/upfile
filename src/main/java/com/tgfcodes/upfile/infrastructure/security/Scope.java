package com.tgfcodes.upfile.infrastructure.security;

public enum Scope {

    READ("files:read"),

    WRITE("files:write");

    private final String value;

    Scope(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}