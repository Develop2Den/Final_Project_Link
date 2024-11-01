package com.finalProject.linkedin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class App {
    public static void main(String[] args) {
//        Dotenv dotenv = Dotenv.load();
        SpringApplication.run(App.class, args);
    }
}
