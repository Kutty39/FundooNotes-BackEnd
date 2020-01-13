package com.blbz.fundooapi.service;

import com.blbz.fundooapi.dto.MsgDto;
import org.springframework.stereotype.Component;

@Component
public interface Publisher {
    void produceMsg(MsgDto msgDto);
}