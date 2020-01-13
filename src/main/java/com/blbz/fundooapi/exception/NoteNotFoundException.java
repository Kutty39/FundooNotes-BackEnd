package com.blbz.fundooapi.exception;

public class NoteNotFoundException extends Exception {
    public NoteNotFoundException(String message) {
        super(message);
    }

    public NoteNotFoundException() {
        super("Note not found");
    }
}
