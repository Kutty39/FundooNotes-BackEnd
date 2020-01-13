package com.blbz.fundooapi.exception;

public class TokenExpiredException extends Exception {
    public TokenExpiredException(String s) {
        super(s);
    }

    public TokenExpiredException() {
        super("Your request has expired. please try login and activate your account");
    }
}
