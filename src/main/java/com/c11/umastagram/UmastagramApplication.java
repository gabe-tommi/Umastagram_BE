package com.c11.umastagram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class UmastagramApplication {

	public static void main(String[] args) {
        // Load .env file and set environment variables
        Dotenv dotenv = Dotenv.configure()
                .directory("./")  // Look for .env in project root
                .ignoreIfMissing() // Don't fail if .env is missing (for Heroku deployment)
                .load();
        
        // Set each variable from .env as a system property so Spring can access them
        dotenv.entries().forEach(entry -> 
            System.setProperty(entry.getKey(), entry.getValue())
        );
        
        SpringApplication.run(UmastagramApplication.class, args);
    }

}
