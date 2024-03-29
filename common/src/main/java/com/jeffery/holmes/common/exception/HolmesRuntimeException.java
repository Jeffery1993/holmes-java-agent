package com.jeffery.holmes.common.exception;

/**
 * Runtime exception for holmes.
 */
public class HolmesRuntimeException extends RuntimeException {

    public HolmesRuntimeException() {
        super();
    }

    public HolmesRuntimeException(String message) {
        super(message);
    }

    public HolmesRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public HolmesRuntimeException(Throwable cause) {
        super(cause);
    }

}
