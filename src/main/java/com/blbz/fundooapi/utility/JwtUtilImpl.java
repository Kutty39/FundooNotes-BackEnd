package com.blbz.fundooapi.utility;

import com.blbz.fundooapi.entiry.UserInfo;
import com.blbz.fundooapi.exception.HeaderMissingException;
import com.blbz.fundooapi.exception.InvalidUserException;
import com.blbz.fundooapi.repository.UserRepo;
import com.blbz.fundooapi.service.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;

@Service
@PropertySource(value = "jwt.properties", ignoreResourceNotFound = true)
@Getter
public class JwtUtilImpl implements JwtUtil {
    private final UserRepo userRepo;
    @Value("${jwt.expiry.time.sec}")
    private long EXPIRY_TIME;
    @Value("${jwt.secret}")
    private String MY_KEY;

    private String userEmail = null;
    private boolean isValid = false;
    private Claims claims;
    private HashMap<String, Object> claimsBody = new HashMap<>();

    @Autowired
    public JwtUtilImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    @Override
    @Cacheable(value="jwtutil",key = "#result",condition = "#result!=null")
    public String generateJwt(String userEmail, String url) {
        claimsBody.put("url", url);
        return Jwts.builder()
                .addClaims(claimsBody)
                .setSubject(userEmail)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRY_TIME))
                .signWith(SignatureAlgorithm.HS512, MY_KEY).compact();
    }

    @Override
    @Cacheable(value="jwtutil",key = "#result",condition = "#result!=null")
    public String generateJwt(String userEmail, int expire, String url) {
        claimsBody.put("url", url);
        return Jwts.builder()
                .addClaims(claimsBody)
                .setSubject(userEmail)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRY_TIME))
                .signWith(SignatureAlgorithm.HS512, MY_KEY).compact();
    }

    @Override
    public boolean isValid() {
        return isValid;
    }

    @Override
    public String userName() {
        return userEmail;
    }

    @Override
    @Caching(evict = {
    @CacheEvict(value = "jwtutil",key = "#token",condition = "#result.valid!=true"),
            @CacheEvict(value = "jwt",key = "#token",condition = "#result.valid!=true")})
    public JwtUtil loadJwt(String token) throws ExpiredJwtException {
        claims = Jwts.parser().setSigningKey(MY_KEY).parseClaimsJws(token).getBody();
        userEmail = claims.getSubject();
        isValid = claims.getExpiration().after(new Date());
        return this;
    }

    @Override
    public Claims getClaims() {
        return claims;
    }

    @Override
    @Cacheable(value = "jwt",key = "#httpServletRequest.getHeader('Authorization').substring(7)")
    public UserInfo validateHeader(HttpServletRequest httpServletRequest) throws InvalidUserException, HeaderMissingException {
        System.out.println("jwt Enter");
        if (httpServletRequest.getHeader("Authorization") != null) {
            String jwt = httpServletRequest.getHeader("Authorization").replace("Bearer ", "");
            String userEmail = loadJwt(jwt).userName();
            UserInfo userInfo = userRepo.findByEid(userEmail);
            if (userInfo == null) {
                throw new InvalidUserException();
            }
            return userInfo;
        }
        throw new HeaderMissingException();
    }


}