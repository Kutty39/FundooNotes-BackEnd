package com.blbz.fundoonotebackend.service;

import com.blbz.fundoonotebackend.dto.LabelDto;
import com.blbz.fundoonotebackend.entiry.Label;
import com.blbz.fundoonotebackend.exception.InvalidUserException;
import com.blbz.fundoonotebackend.exception.LabelNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface LabelService {
    Label createLabel(LabelDto labelDto);

    List<Label> getAllLabels(String jwtToken) throws  InvalidUserException;

    Label getLabel(String labelText,String jwtToken) throws LabelNotFoundException,  InvalidUserException;

    Label editLabel(LabelDto labelDto) throws LabelNotFoundException;
}
