package com.blbz.fundoonotebackend.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class RegisterDto {
    @NotNull(message = "First Name should not be null")
    private String fname;
    @NotNull(message = "Last Name should not be null")
    private String lname;
    @NotNull(message = "Email ID should not be null")
    @Pattern(regexp = "^[a-zA-z\\d._-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,4}$", message = "Please enter valid Email")
    private String eid;
    @NotNull(message = "Phone Number should not be null")
    @Pattern(regexp = "^[0-9]{10}$", message = "Enter valid phone number")
    private String phn;
    @NotNull(message = "Address should not be null")
    private String adrs;
    @NotNull(message = "Password should not be null")
    @Pattern(regexp = "^((?=.*[a-z])(?=.*[A-Z])(?!.*\\s)(?=.*[@$!%*?&])).{8,15}$",
            message = "Password is combination of Upper case,Lower" +
                    "case,Number and Special Character it should be min length of 8 and " +
                    "Max 15")
    private String pas;
    @NotNull(message = "Confirm Password should not be null")
    private String conpas;

}
