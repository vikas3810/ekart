package com.ekart.exception;

public class InSufficientBalanceException extends Exception {

    private static final long serialVersionUID = 1L;


    public InSufficientBalanceException() {

    }

    public InSufficientBalanceException(String msg) {
        super(msg + " " + "Low Balance ");
    }
}
