package com.blbz.fundoonotebackend.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    @Value("${spring.application.name}")
    private String appName;

    @Bean
    public AmazonS3 amazonS3Client(AWSCredentialsProvider credentialsProvider,
                                   @Value("${cloud.aws.region.static}") String region) {
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(credentialsProvider)
                .withRegion(region)
                .build();
    }
}
