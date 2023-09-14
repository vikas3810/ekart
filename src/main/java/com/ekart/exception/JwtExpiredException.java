package com.ekart.exception;

public class JwtExpiredException extends RuntimeException {
    public JwtExpiredException() {
    }

    public JwtExpiredException(String message) {
        super(message);
    }

    public JwtExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}

