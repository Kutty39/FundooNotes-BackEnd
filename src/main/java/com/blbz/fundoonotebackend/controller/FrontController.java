package com.blbz.fundoonotebackend.controller;

import com.blbz.fundoonotebackend.dto.LoginDto;
import com.blbz.fundoonotebackend.dto.RegisterDto;
import com.blbz.fundoonotebackend.dto.ResetPassDto;
import com.blbz.fundoonotebackend.exception.InvalidTokenException;
import com.blbz.fundoonotebackend.exception.InvalidUserException;
import com.blbz.fundoonotebackend.exception.PicNotFoundException;
import com.blbz.fundoonotebackend.exception.TokenExpiredException;
import com.blbz.fundoonotebackend.responce.GeneralResponse;
import com.blbz.fundoonotebackend.service.S3Service;
import com.blbz.fundoonotebackend.service.UserService;
import com.blbz.fundoonotebackend.utility.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@RestController
@Slf4j
public class FrontController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final S3Service s3Service;
    private final GeneralResponse generalResponse;
    private final Util util;

    @Autowired
    public FrontController(UserService userService, AuthenticationManager authenticationManager, S3Service s3Service, GeneralResponse generalResponse, Util util) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.s3Service = s3Service;
        this.generalResponse = generalResponse;
        this.util = util;
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(@RequestHeader("Authorization") String jwtToken) throws Exception {
        generalResponse.setResponse(userService.getAllUser(jwtToken));
        return ResponseEntity.ok().body(generalResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto, BindingResult bindingResult) throws Exception {
        util.validAndThrow(bindingResult);
        try {
            if (!userService.checkEmail(loginDto.getUsername())) {
                throw new InvalidUserException("Bad credential(Username or Password is wrong)");
            }
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new InvalidUserException("Bad credential(Username or Password is wrong)");
        }
        generalResponse.setResponse(userService.loginUser(loginDto.getUsername()));
        return ResponseEntity.ok().body(generalResponse);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDto regData, BindingResult result) throws Exception {
        HashMap<String, List<String>> errorDetail = new HashMap<>();
        List<String> errors = new ArrayList<>();
        util.validAndThrow(result);
        if (regData.getPas() != null) {
            if (!regData.getPas().equals(regData.getConpas())) {
                errors.add("Password and Confirm password should be equal");
            }
        }
        if (userService.checkEmail(regData.getEid())) {
            errors.add("Email already exists.");
        }
        if (errors.size() > 0) {
            errorDetail.put("Errors", errors);
            log.info(String.valueOf(errors));
            throw new Exception(String.valueOf(errorDetail));
        }
        //try {
        return ResponseEntity.ok().body(userService.registerUser(regData));
        /*} catch (MessagingException e) {
            return ResponseEntity.badRequest().body("Something went wrong. please try register again");
        }*/
    }


    @GetMapping("/email/{email}")
    public ResponseEntity<?> validEmail(@PathVariable String email) {
        generalResponse.setResponse(userService.checkEmail(email));
        return ResponseEntity.ok(generalResponse);
    }

    @GetMapping("/activate/{jwt}")
    public ResponseEntity<?> activateUser(@PathVariable String jwt) throws TokenExpiredException, InvalidTokenException {
        generalResponse.setResponse(userService.userActivate(jwt));
        return ResponseEntity.ok().body(generalResponse);
    }

    @PutMapping("/blockjwt/{jwt}")
    public ResponseEntity<?> blockJwt(@PathVariable String jwt) {
        userService.blockedJwt(jwt);
        generalResponse.setResponse(true);
        return ResponseEntity.ok().body(generalResponse);
    }

    @GetMapping("/forgotpassword/{email}")
    public ResponseEntity<?> forgotPassword(@PathVariable String email) throws Exception {
        if (userService.checkEmail(email)) {
            generalResponse.setResponse(userService.forgotPasswordMail(email));
            return ResponseEntity.ok().body(generalResponse);
        } else {
            generalResponse.setResponse("email not found");
            return ResponseEntity.badRequest().body(generalResponse);
        }

    }

    @PostMapping("/resetpassword")
    public ResponseEntity<?> resetPass(@Valid @RequestBody ResetPassDto resetPassDto, BindingResult bindingResult, @RequestHeader("Authorization") String header) {
        util.validAndThrow(bindingResult);
        if (!resetPassDto.getPassword().equals(resetPassDto.getConpassword())) {
            return ResponseEntity.badRequest().body("Password and conform is not matched");
        }
        userService.updatePassword(header, resetPassDto.getPassword());
        generalResponse.setResponse("Successfully resetted password. Please login again");
        return ResponseEntity.ok().body(generalResponse);
    }

    @PostMapping("/api/uploadpic")
    public ResponseEntity<?> uploadPic(@RequestParam String filePath, @RequestHeader("Authorization") String header) throws InvalidUserException {
        s3Service.uploadFile(filePath, header);
        generalResponse.setResponse("Uploaded");
        return ResponseEntity.ok().body(generalResponse);
    }

    @PostMapping(value = "/api/uploadFile", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> uploadPicfile(@RequestParam String file, @RequestHeader("Authorization") String header) throws InvalidUserException, PicNotFoundException, IOException {
        generalResponse.setResponse(s3Service.uploadFileWithFile(file, header));
        return ResponseEntity.ok().body(generalResponse);
    }

    @GetMapping("/api/downloadpic")
    public ResponseEntity<?> downloadPic(@RequestHeader("Authorization") String header) throws InvalidUserException, PicNotFoundException {
        generalResponse.setResponse(s3Service.downloadFile(header).toString());
        return ResponseEntity.ok().body(generalResponse);
    }
}
