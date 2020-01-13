package com.blbz.fundooapi.exception;

public class InvalidNoteStatus extends Exception {
    public InvalidNoteStatus(String s) {
        super(s);
    }

    public InvalidNoteStatus() {
        super("Invalid note status");
    }
}

