package com.blbz.fundooapi.exception;

public class InvalidUserException extends Exception {
    public InvalidUserException(String message) {
        super(message);
    }

    public InvalidUserException() {
        super("Invalid User");
    }
}
