package com.blbz.fundoonotebackend.exception;

public class LabelNotFoundException extends Exception  {
    public LabelNotFoundException() {
        super("Label not found");
    }

    public LabelNotFoundException(String s) {
        super(s);
    }

}
