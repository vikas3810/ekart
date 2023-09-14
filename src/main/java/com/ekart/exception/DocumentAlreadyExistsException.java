package com.ekart.exception;

public class DocumentAlreadyExistsException extends Throwable {

    public DocumentAlreadyExistsException() {

    }
    public DocumentAlreadyExistsException(String msg) {
        super(msg);
    }
}
