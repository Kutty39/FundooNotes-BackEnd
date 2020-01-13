package com.blbz.fundooapi.service;

import com.blbz.fundooapi.dto.LabelDto;
import com.blbz.fundooapi.entiry.Label;
import com.blbz.fundooapi.exception.HeaderMissingException;
import com.blbz.fundooapi.exception.InvalidUserException;
import com.blbz.fundooapi.exception.LabelNotFoundException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
public interface LabelService {
    Label createLabel(LabelDto labelDto);

    List<Label> getAllLabels(HttpServletRequest request) throws HeaderMissingException, InvalidUserException;

    Label getLabel(String lableText, HttpServletRequest request) throws LabelNotFoundException, HeaderMissingException, InvalidUserException;

    Label editLabel(LabelDto labelDto) throws LabelNotFoundException;
}
