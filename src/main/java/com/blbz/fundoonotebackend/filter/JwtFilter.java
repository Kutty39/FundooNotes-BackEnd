package com.blbz.fundoonotebackend.filter;

import com.blbz.fundoonotebackend.config.security.MyUserDetailService;
import com.blbz.fundoonotebackend.dto.BlockedJwt;
import com.blbz.fundoonotebackend.exception.InvalidTokenException;
import com.blbz.fundoonotebackend.exception.TokenExpiredException;
import com.blbz.fundoonotebackend.service.JwtUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private MyUserDetailService myUserDetailService;
    private JwtUtil jwtUtil;
    private BlockedJwt blockedJwt;

    @Autowired
    public JwtFilter(BlockedJwt blockedJwt
            , MyUserDetailService myUserDetailService
            , JwtUtil jwtUtil) {
        super();
        this.blockedJwt = blockedJwt;
        this.myUserDetailService = myUserDetailService;
        this.jwtUtil = jwtUtil;
    }

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            jwtUtil=jwtUtil.loadJwt(header);
            if (!blockedJwt.getBJwt().contains(header)) {
                if (jwtUtil.isValid()) {
                    if (jwtUtil.getClaims().get("url").equals("api")) {
                        UserDetails userDetails = myUserDetailService.loadUserByUsername(jwtUtil.userName());
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    } else {
                        throw new InvalidTokenException("Invalid token");
                    }
                } else {
                    throw new TokenExpiredException("Token expired");
                }

            } else {
                throw new IllegalAccessException("You are using blocked token");
            }
        }
        filterChain.doFilter(request, response);
    }
}