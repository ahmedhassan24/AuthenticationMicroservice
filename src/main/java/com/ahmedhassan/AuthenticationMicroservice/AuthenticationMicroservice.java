package com.ahmedhassan.AuthenticationMicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class AuthenticationMicroservice {

    public static void main(String[] args) {
        SpringApplication.run(AuthenticationMicroservice.class, args);
    }

}
