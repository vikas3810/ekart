package com.ekart.exception;

public class UnAuthorizedUserException extends Exception{
    public UnAuthorizedUserException() {
    }

    public UnAuthorizedUserException(String message) {
        super(message);
    }
}
