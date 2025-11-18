package com.exam.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Graduate Exam Study Platform Application
 * Main entry point for the Spring Boot application
 */
@SpringBootApplication
@EnableCaching
@EnableJpaAuditing
public class ExamPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExamPlatformApplication.class, args);
    }
}
