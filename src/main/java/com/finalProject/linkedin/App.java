package com.finalProject.linkedin;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class App {
    public static void main(String[] args) {
        if (!isRunningLocally()) {
            Dotenv dotenv = Dotenv.configure().load();
            dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
        }
        SpringApplication.run(App.class, args);
    }

    private static boolean isRunningLocally() {
        return "false".equals(System.getenv("RUNNING_LOCALLY"));
    }
}
