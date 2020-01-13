package com.blbz.fundooapi.config.security;

import com.blbz.fundooapi.entiry.UserInfo;
import com.blbz.fundooapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class MyUserDetailService implements UserDetailsService {
    private final UserService userService;
    private UserInfo userInfo;

    @Autowired
    public MyUserDetailService(UserInfo userInfo, UserService userService) {
        this.userInfo = userInfo;
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        userInfo = userService.getUser(s);
        return new User(userInfo.getEid(), userInfo.getPas(), new ArrayList<>());
    }

}
