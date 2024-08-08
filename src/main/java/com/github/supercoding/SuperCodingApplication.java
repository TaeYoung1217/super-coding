package com.github.supercoding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class SuperCodingApplication {
    public static void main(String[] args) {
        SpringApplication.run(SuperCodingApplication.class, args);
    }

}
