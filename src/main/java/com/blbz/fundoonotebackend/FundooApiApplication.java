package com.blbz.fundoonotebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.blbz.fundoonotebackend")
@EnableCaching
public class FundooApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FundooApiApplication.class, args);
    }

}
