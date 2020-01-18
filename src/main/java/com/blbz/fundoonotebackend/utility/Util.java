package com.blbz.fundoonotebackend.utility;

import com.blbz.fundoonotebackend.dto.BlockedJwt;
import com.blbz.fundoonotebackend.dto.MsgDto;
import com.blbz.fundoonotebackend.responce.GeneralResponse;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.BindingResult;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.ValidationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Util {
    private JavaMailSender sender;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    public Util(JavaMailSender sender) {
        this.sender = sender;
    }



    public String encoder(String str) {
        return encoder.encode(str);
    }

    public boolean passwordMatcher(String dtoPas, String dbPas) {

        return encoder.matches(dtoPas, dbPas);
    }

    public void sendEmail(MsgDto msgDto) throws MessagingException {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo(msgDto.getEmail());
        helper.setText(msgDto.getMsg(), true);
        helper.setSubject(msgDto.getSubject());
        sender.send(message);
    }

    public String getMsg(String msgName) {
        try {
            File file=null;
            switch (msgName.toLowerCase()){
                case "forgot":
                    file=ResourceUtils.getFile("classpath:files/forgotMsg.txt");
                    break;
                case "activate":
                    file=ResourceUtils.getFile("classpath:files/emailMsg.txt");
                    break;
                default:
                    return null;
            }
            return new String(Files.readAllBytes(file.toPath()));
        } catch (Exception e) {
            System.out.println("error " + e.getMessage());
            return null;
        }
    }

    @Bean
    @Cacheable(value = "blkJwt")
    public BlockedJwt getBlockedJwt() {
        Gson gson = new Gson();
        try {
            File file =ResourceUtils.getFile("classpath:files/block.json");
            if (!file.createNewFile()) {
                String blockedJwtString = new String(Files.readAllBytes(file.toPath()));
                return gson.fromJson(blockedJwtString, BlockedJwt.class);
            } else {
                return new BlockedJwt();
            }
        } catch (Exception ex) {
            System.out.println("Error " + ex.getMessage());
            return new BlockedJwt();
        }

    }

    public void writeBlockJwt(BlockedJwt blockedJwt) {
        Gson gson = new Gson();
        try {
            File file=ResourceUtils.getFile("classpath:files/block.json");
            FileWriter writer = new FileWriter(file);
            writer.write(gson.toJson(blockedJwt));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void validAndThrow(BindingResult result) {
        if(result.hasErrors()) {
            List<String> errors;
            errors = (result.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList()));
            throw new ValidationException(String.valueOf(new GeneralResponse(errors)));
        }
    }
}
