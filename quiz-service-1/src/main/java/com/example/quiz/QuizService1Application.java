package com.example.quiz;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class QuizService1Application implements CommandLineRunner { // Implement CommandLineRunner

    @Value("${custom.config.source}")
    private String configSource;

    public static void main(String[] args) {
        SpringApplication.run(QuizService1Application.class, args);
    }

    // This method runs once the application is started
    @Override
    public void run(String... args) throws Exception {
        System.out.println("---------------------------------------------");
        System.out.println("CONFIG SOURCE TEST VALUE: " + configSource);
        System.out.println("---------------------------------------------");
    }
}