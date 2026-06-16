package com.tgfcodes.upfile.domain;

import java.util.Map;
import java.util.function.Supplier;

import static java.util.Objects.isNull;

public class Checks {

    private Checks() {
        throw new IllegalStateException("Utility class");
    }

    public static void requireNonNull(Object object, Supplier<? extends RuntimeException> exceptionSupplier) {
        if (isNull(object)) throw exceptionSupplier.get();
    }

    public static void requireNonEmpty(String value, Supplier<? extends RuntimeException> exceptionSupplier) {
        if (isNull(value) || value.isBlank()) throw exceptionSupplier.get();
    }

    public static void requireNonEmpty(Map<?, ?> value, Supplier<? extends RuntimeException> exceptionSupplier) {
        if (isNull(value) || value.isEmpty()) throw exceptionSupplier.get();
    }

    public static void requirePositive(Integer ttlSeconds, Supplier<? extends RuntimeException> exceptionSupplier) {
        if (isNull(ttlSeconds) || ttlSeconds <= 0) throw exceptionSupplier.get();
    }
}