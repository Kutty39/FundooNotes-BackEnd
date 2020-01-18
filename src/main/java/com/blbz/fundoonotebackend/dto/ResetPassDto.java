package com.blbz.fundoonotebackend.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class ResetPassDto {
    @NotNull(message = "Password should not be null")
    @Pattern(regexp = "^((?=.*[a-z])(?=.*[A-Z])(?!.*\\s)(?=.*[@$!%*?&])).{8,15}$",
            message = "Password is combination of Upper case,Lower" +
                    "case,Number and Special Character it should be min length of 8 and " +
                    "Max 15")
    private String password;
    @NotNull(message = "Confirm Password should not be null")
    private String conpassword;
}
