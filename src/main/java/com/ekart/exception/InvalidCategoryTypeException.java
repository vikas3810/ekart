package com.ekart.exception;

public class InvalidCategoryTypeException extends Throwable {

    public InvalidCategoryTypeException() {

    }
    public InvalidCategoryTypeException(String msg) {
        super(msg);
    }
}
