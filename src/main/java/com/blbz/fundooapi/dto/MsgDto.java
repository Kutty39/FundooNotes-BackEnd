package com.blbz.fundooapi.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Component
public class MsgDto {
    private String name;
    private String subject;
    @NotNull(message = "Email ID should not be null")
    @Pattern(regexp = "^[a-zA-z\\d._-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,4}$", message = "Please enter valid Email")
    private String email;
    private String msg;
    private String jwt;
}
