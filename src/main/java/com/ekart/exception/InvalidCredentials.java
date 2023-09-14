package com.ekart.exception;

public class InvalidCredentials extends Exception {

    private static final long serialVersionUID = 1L;

    public InvalidCredentials(String message) {
        super(message);
    }

    public InvalidCredentials() {
    }
}
