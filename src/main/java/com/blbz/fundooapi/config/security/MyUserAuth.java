package com.blbz.fundooapi.config.security;

import com.blbz.fundooapi.entiry.UserInfo;
import com.blbz.fundooapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
public class MyUserAuth implements UserDetails {
    @Autowired
    private UserInfo userInfo;
    @Autowired
    private UserService userService;

    public MyUserAuth(String username) {
        this.userInfo = userService.getUser(username);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public String getPassword() {
        return userInfo.getPas();
    }

    @Override
    public String getUsername() {
        return userInfo.getEid();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}