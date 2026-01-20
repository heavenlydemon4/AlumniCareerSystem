package com.alumni.career;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AlumniCareerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlumniCareerApplication.class, args);
        System.out.println("==============================================");
        System.out.println("Alumni Career Management System is running!");
        System.out.println("API Available at: http://localhost:8080/api");
        System.out.println("==============================================");
    }
}
