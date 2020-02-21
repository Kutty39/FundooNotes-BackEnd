package com.blbz.fundoonotebackend.serviceimpl;

import com.blbz.fundoonotebackend.dto.BlockedJwt;
import com.blbz.fundoonotebackend.dto.LoginDto;
import com.blbz.fundoonotebackend.dto.MsgDto;
import com.blbz.fundoonotebackend.dto.RegisterDto;
import com.blbz.fundoonotebackend.entiry.UserInfo;
import com.blbz.fundoonotebackend.entiry.UserStatus;
import com.blbz.fundoonotebackend.exception.InvalidTokenException;
import com.blbz.fundoonotebackend.exception.InvalidUserException;
import com.blbz.fundoonotebackend.exception.TokenExpiredException;
import com.blbz.fundoonotebackend.repository.jpa.UserRepo;
import com.blbz.fundoonotebackend.service.JwtUtil;
import com.blbz.fundoonotebackend.service.Publisher;
import com.blbz.fundoonotebackend.service.UserService;
import com.blbz.fundoonotebackend.service.UserStatusService;
import com.blbz.fundoonotebackend.utility.DtoMapper;
import com.blbz.fundoonotebackend.utility.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final UserStatusService userStatusService;
    private final Util util;
    private final Publisher publisher;
    private final JwtUtil jwtUtil;
    private final MsgDto msgDto;
    private final BlockedJwt blockedJwt;
    private final UserInfo userInfo;
    private final DtoMapper dtoMapper;
    @Value("${jwt.expiry.time.sec.day}")
    private int expireForDay;
    @Value("${frontend.server.host}")
    private String SERVERNAME;
    private String msgBody;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, UserStatusService userStatusService
            , Util util
            , Publisher publisher
            , JwtUtil jwtUtil
            , MsgDto msgDto
            , BlockedJwt blockedJwt, UserInfo userInfo, DtoMapper dtoMapper) {
        this.userRepo = userRepo;
        this.userStatusService = userStatusService;
        this.util = util;
        this.publisher = publisher;
        this.jwtUtil = jwtUtil;
        this.msgDto = msgDto;
        this.blockedJwt = blockedJwt;
        this.userInfo = userInfo;
        this.dtoMapper = dtoMapper;
    }


    @Override
    public String registerUser(RegisterDto registerDto) throws Exception {
        registerDto.setPas(util.encoder(registerDto.getPas()));

        BeanUtils.copyProperties(registerDto, userInfo);
        UserStatus status = userStatusService.getByStatus("Inactive");
        userInfo.setUserStatus(status);
        userInfo.setUserCreatedOn(Date.from(Instant.now()));
        userRepo.save(userInfo);
        return sendActivationMail(registerDto.getEid(), registerDto.getFname() + " " + registerDto.getLname());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkEmail(String email) {
        return userRepo.findByUniqKey(email) != null;
    }

    @Override
    @Transactional(readOnly = true)
    public UserInfo getUser(String useremail) {
        return userRepo.findByUniqKey(useremail);
    }

    @Override
    public boolean passwordMatcher(LoginDto loginDto) {
        UserInfo userInfo = userRepo.findByUniqKey(loginDto.getUsername());
        if (userInfo != null) {
            return util.passwordMatcher(loginDto.getPassword(), userInfo.getPas());
        } else {
            return false;
        }
    }

    @Override
    public String sendActivationMail(String email, String fullname) throws Exception {
        msgDto.setEmail(email);
        msgDto.setJwt(jwtUtil.generateJwt(email, expireForDay, "activate"));
        msgDto.setName(fullname);
        msgDto.setSubject("Account Activation");

        msgBody = util.getMsg("activate");
        return msgFormatter();
    }

    @Override
    @Transactional
    public String userActivate(String jwt) throws InvalidTokenException, TokenExpiredException {
        jwtUtil.loadJwt(jwt);
        if (jwtUtil.isValid()) {
            if (jwtUtil.getClaims().get("url").equals("activate")) {
                UserInfo userInfo = userRepo.findByUniqKey(jwtUtil.userName());
                if (userInfo.getUserStatus().getStatusText().equals("Active")) {
                    return "Account already activated";
                } else {
                    userInfo.setUserStatus(userStatusService.getByStatus("Active"));
                    userInfo.setUserLastModifiedOn(Date.from(Instant.now()));
                    userRepo.save(userInfo);
                    blockedJwt(jwt);
                    return "Your account has been activated";
                }
            } else {
                throw new InvalidTokenException("Invalid token. please try login and activate your account");
            }
        } else {
            throw new TokenExpiredException();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public String loginUser(String userEmail) throws Exception {
        UserInfo userInfo = userRepo.findByUniqKey(userEmail);
        if(userInfo==null){
            throw new InvalidUserException("Email or Password is wrong");
        }
        String status = userInfo.getUserStatus().getStatusText();
        if (status.equals("Active")) {
            return (jwtUtil.generateJwt(userEmail, "api"));
        } else if (status.equals("Closed")) {
            throw new InvalidUserException("You are trying to access closed account. Please register again");
        } else {
            String str=sendActivationMail(userEmail, userInfo.getFname() + " " + userInfo.getLname());
            throw new InvalidUserException("Your account is not activated yet. We sent activation mail to your mail."+ str);
        }
    }

    @Override
    public void blockedJwt(String jwt) {
        blockedJwt.getBJwt().add(jwt);
        util.writeBlockJwt(blockedJwt);
    }

    @Override
    public String forgotPasswordMail(String email) throws Exception {
        UserInfo userInfo = userRepo.findByUniqKey(email);
        msgDto.setSubject("Reset password");
        msgDto.setName(userInfo.getFname() + " " + userInfo.getLname());
        msgDto.setEmail(email);
        msgDto.setJwt(jwtUtil.generateJwt(email, expireForDay, "api"));
        msgBody = util.getMsg("forgot");
        return msgFormatter();
    }

    @Override
    public void updatePassword(String jwt, String pas) {
        jwtUtil.loadJwt(jwt);
        UserInfo userInfo = userRepo.findByUniqKey(jwtUtil.userName());
        userInfo.setPas(util.encoder(pas));
        blockedJwt(jwt);
        userRepo.save(userInfo);
    }

    @Override
    public List<String> getAllUser(String jwtToken) throws  InvalidUserException {
        if (jwtUtil.validateHeader(jwtToken) != null) {
            Stream<UserInfo> userInfoElStream= StreamSupport.stream(userRepo.findAll().spliterator(),false);
            return userInfoElStream.map(UserInfo::getEid).collect(Collectors.toList());
        }
        return null;
    }


    private String msgFormatter() throws Exception {
        if (msgBody != null) {
            msgBody = msgBody.replace("{name}", msgDto.getName());
            msgBody = msgBody.replace("{jwt}", msgDto.getJwt());
            msgBody = msgBody.replace("{servername}", SERVERNAME);
            msgDto.setMsg(msgBody);
            publisher.produceMsg(msgDto);
            return "Please check your email.";
        }
        throw new Exception("Something went wrong");
    }

}
