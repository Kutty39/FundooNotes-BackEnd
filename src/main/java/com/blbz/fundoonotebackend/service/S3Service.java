package com.blbz.fundoonotebackend.service;

import com.blbz.fundoonotebackend.exception.InvalidUserException;
import com.blbz.fundoonotebackend.exception.PicNotFoundException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

@Component
public interface S3Service {
    void uploadFile(String filepath, String header) throws InvalidUserException;
    URL downloadFile(String header) throws InvalidUserException, PicNotFoundException;

    URL uploadFileWithFile(String file, String header) throws InvalidUserException, PicNotFoundException, IOException;
}
