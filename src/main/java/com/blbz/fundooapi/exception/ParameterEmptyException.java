package com.blbz.fundooapi.exception;

public class ParameterEmptyException extends Exception {
    public ParameterEmptyException(String s) {
    }

    public ParameterEmptyException() {
        super("Parameter field is empty");
    }
}
