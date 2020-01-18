package com.blbz.fundoonotebackend.exception;

public class ParameterEmptyException extends Exception {
    public ParameterEmptyException(String s) {
    }

    public ParameterEmptyException() {
        super("Parameter field is empty");
    }
}
