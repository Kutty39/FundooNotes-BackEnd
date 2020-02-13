package com.blbz.fundoonotebackend.exception;

public class InvalidNoteException extends Exception {
    public InvalidNoteException(String s){
        super(s);
    }
    public InvalidNoteException(){
        super("Any one of the fields is mandatory." +
                "Title,Note Text, Remainder or Collaborator");
    }
}
