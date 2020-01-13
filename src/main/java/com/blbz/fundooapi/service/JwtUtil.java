package com.blbz.fundooapi.service;

import com.blbz.fundooapi.entiry.UserInfo;
import com.blbz.fundooapi.exception.HeaderMissingException;
import com.blbz.fundooapi.exception.InvalidUserException;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public interface JwtUtil {
    String generateJwt(String userEmail,String url);
    String generateJwt(String userEmail,int expire,String url);
    UserInfo validateHeader(HttpServletRequest httpServletRequest) throws InvalidUserException, HeaderMissingException;
    boolean isValid();

    String userName();

    JwtUtil loadJwt(String token);
    Claims getClaims();
}
