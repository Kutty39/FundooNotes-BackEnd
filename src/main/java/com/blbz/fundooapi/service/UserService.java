package com.blbz.fundooapi.service;

import com.blbz.fundooapi.dto.LoginDto;
import com.blbz.fundooapi.dto.RegisterDto;
import com.blbz.fundooapi.entiry.UserInfo;
import com.blbz.fundooapi.exception.InvalidTokenException;
import com.blbz.fundooapi.exception.TokenExpiredException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
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

    List<UserInfo> getAllUser(HttpServletRequest httpServletRequest) throws Exception;
}
