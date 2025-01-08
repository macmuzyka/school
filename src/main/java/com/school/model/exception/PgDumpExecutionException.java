package com.school.model.exception;

public class PgDumpExecutionException extends RuntimeException {
    public PgDumpExecutionException(final Throwable cause) {
        super(cause);
    }
}
