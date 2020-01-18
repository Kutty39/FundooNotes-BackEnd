package com.blbz.fundoonotebackend.service;

import com.blbz.fundoonotebackend.dto.LoginDto;
import com.blbz.fundoonotebackend.dto.RegisterDto;
import com.blbz.fundoonotebackend.entiry.UserInfo;
import com.blbz.fundoonotebackend.exception.InvalidTokenException;
import com.blbz.fundoonotebackend.exception.TokenExpiredException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserService {
    String registerUser(RegisterDto registerDto) throws Exception;
    boolean checkEmail(String email);
    UserInfo getUser(String useremail);
    boolean passwordMatcher(LoginDto loginDto);
    String sendActivationMail( String eid, String msgDto) throws Exception;
    String userActivate(String jwt) throws InvalidTokenException, TokenExpiredException;
    String loginUser(String username) throws Exception;
    void blockedJwt(String jwt);
    String forgotPasswordMail(String email) throws Exception;
    void updatePassword(String jwt,String pas);

    List<UserInfo> getAllUser(String jwtToken) throws Exception;
}
