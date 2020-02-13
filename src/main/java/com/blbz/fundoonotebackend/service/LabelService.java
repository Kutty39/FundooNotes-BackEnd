package com.blbz.fundoonotebackend.service;

import com.blbz.fundoonotebackend.entiry.Label;
import com.blbz.fundoonotebackend.exception.InvalidUserException;
import com.blbz.fundoonotebackend.exception.LabelNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface LabelService {
    String createLabel(String labelText,String jwtToken) throws InvalidUserException;
    Label createLabelandGet(String labelText,String jwtToken) throws InvalidUserException;

    List<String> getAllLabels(String jwtToken) throws  InvalidUserException;

    String getLabel(String labelText, String jwtToken) throws LabelNotFoundException,  InvalidUserException;

    String editLabel(String oldLabel,String newLabel,String jwtToken) throws LabelNotFoundException, InvalidUserException;

    void deleteLabel(String labelText, String jwtToken) throws InvalidUserException;
}
