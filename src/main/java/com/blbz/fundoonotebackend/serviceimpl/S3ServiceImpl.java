package com.blbz.fundoonotebackend.serviceimpl;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.blbz.fundoonotebackend.entiry.ProfilePic;
import com.blbz.fundoonotebackend.entiry.UserInfo;
import com.blbz.fundoonotebackend.exception.InvalidUserException;
import com.blbz.fundoonotebackend.exception.PicNotFoundException;
import com.blbz.fundoonotebackend.repository.jpa.PicRepo;
import com.blbz.fundoonotebackend.service.JwtUtil;
import com.blbz.fundoonotebackend.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

@Service
public class S3ServiceImpl implements S3Service {
    private final JwtUtil jwtUtil;
    private final AmazonS3Client amazonS3Client;
    private final PicRepo picRepo;
    private ProfilePic profilePic;
    @Value("${s3.bucketName}")
    private String bucketName;

    @Autowired
    public S3ServiceImpl(JwtUtil jwtUtil, AmazonS3Client amazonS3Client, PicRepo picRepo, ProfilePic profilePic) {
        this.jwtUtil = jwtUtil;
        this.amazonS3Client = amazonS3Client;
        this.picRepo = picRepo;
        this.profilePic = profilePic;
    }

    @Override
    public void uploadFile(String filePath, String header) throws InvalidUserException {
        UserInfo userInfo = jwtUtil.validateHeader(header);
        File file = new File(filePath);
        ProfilePic profilePic1 = picRepo.findByCreatedBy(userInfo);
        if (profilePic1 == null) {
            profilePic.setPicID(0);
            profilePic.setCreatedBy(userInfo);
        } else {
            profilePic = profilePic1;
            amazonS3Client.deleteObject(bucketName, profilePic.getFilePath());
        }
        amazonS3Client.putObject(new PutObjectRequest(bucketName, userInfo.getEid() + "/" + file.getName(), file));
        profilePic.setFilePath(userInfo.getEid() + "/" + file.getName());
        picRepo.save(profilePic);
    }

    @Override
    public URL downloadFile(String header) throws InvalidUserException, PicNotFoundException {
        UserInfo userInfo = jwtUtil.validateHeader(header);
        profilePic = picRepo.findByCreatedBy(userInfo);
        if (profilePic != null) {
            Date date = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DATE, 1);
            Date expiration = c.getTime();
            return amazonS3Client.generatePresignedUrl(bucketName, profilePic.getFilePath(), expiration);
        } else {
            throw new PicNotFoundException();
        }
    }

    @Override
    public URL uploadFileWithFile(String file, String header) throws InvalidUserException, PicNotFoundException, IOException {
        String frmt = file.split(";")[0].replace("data:image/", "");
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] imageByte = decoder.decodeBuffer(file.split(",")[1]);
        ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
        BufferedImage image = ImageIO.read(bis);
        bis.close();
        File outputfile = new File("image." + frmt);
        ImageIO.write(image, frmt, outputfile);
        UserInfo userInfo = jwtUtil.validateHeader(header);
        ProfilePic profilePic1 = picRepo.findByCreatedBy(userInfo);
        if (profilePic1 == null) {
            profilePic.setPicID(0);
            profilePic.setCreatedBy(userInfo);
        } else {
            profilePic = profilePic1;
            amazonS3Client.deleteObject(bucketName, profilePic.getFilePath());
        }
        amazonS3Client.putObject(new PutObjectRequest(bucketName, userInfo.getEid() + "/" + outputfile.getName(), outputfile));
        profilePic.setFilePath(userInfo.getEid() + "/" + outputfile.getName());
        picRepo.save(profilePic);
        return downloadFile(header);
    }
}
