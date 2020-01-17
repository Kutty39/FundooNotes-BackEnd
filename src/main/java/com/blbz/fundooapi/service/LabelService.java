package com.blbz.fundooapi.service;

import com.blbz.fundooapi.dto.LabelDto;
import com.blbz.fundooapi.entiry.Label;
import com.blbz.fundooapi.exception.InvalidUserException;
import com.blbz.fundooapi.exception.LabelNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface LabelService {
    Label createLabel(LabelDto labelDto);

    List<Label> getAllLabels(String jwtToken) throws  InvalidUserException;

    Label getLabel(String labelText,String jwtToken) throws LabelNotFoundException,  InvalidUserException;

    Label editLabel(LabelDto labelDto) throws LabelNotFoundException;
}
