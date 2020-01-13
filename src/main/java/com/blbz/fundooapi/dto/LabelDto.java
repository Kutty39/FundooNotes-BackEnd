package com.blbz.fundooapi.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class LabelDto {
    private int labelId;
    private String labelText;
}
