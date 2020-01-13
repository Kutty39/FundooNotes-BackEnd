package com.blbz.fundooapi.dto;

import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Component
public class BlockedJwt implements Serializable {
    private List<String> bJwt=new ArrayList<>();
}
