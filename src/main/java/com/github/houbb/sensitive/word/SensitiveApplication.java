package com.github.houbb.sensitive.word;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class SensitiveApplication extends SpringBootServletInitializer {
    public static void main(String[] args){
        SpringApplication.run(SensitiveApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(SensitiveApplication.class);
    }

}
