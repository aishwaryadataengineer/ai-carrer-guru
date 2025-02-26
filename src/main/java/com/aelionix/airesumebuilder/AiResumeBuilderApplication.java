package com.aelionix.airesumebuilder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AiResumeBuilderApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiResumeBuilderApplication.class, args);
    }

}
