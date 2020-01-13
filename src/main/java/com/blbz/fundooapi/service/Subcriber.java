package com.blbz.fundooapi.service;

import com.blbz.fundooapi.dto.MsgDto;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;

@Component
public interface Subcriber {
    void getMessage(MsgDto msgDto) throws MessagingException;
}