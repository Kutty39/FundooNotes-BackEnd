package com.blbz.fundoonotebackend.service;

import com.blbz.fundoonotebackend.dto.MsgDto;
import org.springframework.stereotype.Component;

@Component
public interface Publisher {
    void produceMsg(MsgDto msgDto);
}