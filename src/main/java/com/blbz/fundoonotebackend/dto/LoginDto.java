package com.blbz.fundoonotebackend.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class LoginDto {
    @NotNull(message = "Email ID should not be null")
    @Pattern(regexp = "^[a-zA-z\\d._-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,4}$", message = "Please enter valid Email")
    private String username;
    @NotNull(message = "Password should not be null")
    private String password;
}
