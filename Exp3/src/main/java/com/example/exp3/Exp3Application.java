package com.example.exp3;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.example.exp3.mapper")
@SpringBootApplication
public class Exp3Application {

    public static void main(String[] args) {
        try {
            SpringApplication.run(Exp3Application.class, args);
        } catch (Exception e) {
            System.err.println("Application startup failed: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}