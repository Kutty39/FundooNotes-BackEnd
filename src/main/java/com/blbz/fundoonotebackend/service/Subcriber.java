package com.blbz.fundoonotebackend.service;

import com.blbz.fundoonotebackend.dto.MsgDto;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;

@Component
public interface Subcriber {
    void getMessage(MsgDto msgDto) throws MessagingException;
}