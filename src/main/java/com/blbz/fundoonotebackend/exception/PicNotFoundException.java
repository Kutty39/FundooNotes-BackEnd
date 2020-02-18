package com.blbz.fundoonotebackend.exception;

public class PicNotFoundException extends Exception {
    public PicNotFoundException(String s){
        super(s);
    }
    public PicNotFoundException(){
        super("Picture not found");
    }
}
