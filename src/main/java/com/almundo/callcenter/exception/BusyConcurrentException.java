package com.almundo.callcenter.exception;

/**
 * for HTTP 400 errors
 */
public final class BusyConcurrentException extends RuntimeException {
    public BusyConcurrentException() {
        super();
    }

    public BusyConcurrentException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public BusyConcurrentException(final String message) {
        super(message);
    }

    public BusyConcurrentException(final Throwable cause) {
        super(cause);
    }
}