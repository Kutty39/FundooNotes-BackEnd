package com.blbz.fundoonotebackend.utility;

import com.blbz.fundoonotebackend.entiry.UserInfo;
import com.blbz.fundoonotebackend.exception.InvalidUserException;
import com.blbz.fundoonotebackend.repository.jpa.UserRepo;
import com.blbz.fundoonotebackend.service.JwtUtil;
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
                .setExpiration(new Date(System.currentTimeMillis() + expire))
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
    @CacheEvict(value = "jwtutil",key = "#token",condition = "#result.isValid()!=true"),
            @CacheEvict(value = "jwt",key = "#token",condition = "#result.isValid()!=true")})
    public JwtUtil loadJwt(String token) throws ExpiredJwtException {
        claims = Jwts.parser().setSigningKey(MY_KEY).parseClaimsJws(token.replace("Bearer ","")).getBody();
        userEmail = claims.getSubject();
        isValid = claims.getExpiration().after(new Date());
        return this;
    }

    @Override
    public Claims getClaims() {
        return claims;
    }

    @Override
    @Cacheable(value = "jwt",key = "#jwtHeader")
    public UserInfo validateHeader(String jwtHeader) throws InvalidUserException {
        loadJwt(jwtHeader);
        String userEmail = getUserEmail();
            UserInfo userInfo = userRepo.findByUniqKey(userEmail);
            if (userInfo == null) {
                throw new InvalidUserException();
            }
            return userInfo;
    }


}