package com.blbz.fundooapi.exception;

public class HeaderMissingException extends Exception {
    public HeaderMissingException(String message) {
        super(message);
    }

    public HeaderMissingException() {
        super("Authorization header is missing");
    }
}
