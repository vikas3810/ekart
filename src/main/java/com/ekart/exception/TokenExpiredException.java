package com.ekart.exception;

public class TokenExpiredException extends Throwable{
    public TokenExpiredException(String tokenHasExpired) {
        super(tokenHasExpired);
    }
}
