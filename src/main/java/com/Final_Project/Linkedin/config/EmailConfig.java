package com.Final_Project.Linkedin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

    @Bean
    public JavaMailSender javaMailSender() {

        String USERNAME = "FinalProjectLinkedin@gmail.com";
        String PASSWORD = "y j p d b u g n m t s d o p t i";

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        // Настройки почтового сервера Gmail
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername(USERNAME);
        mailSender.setPassword(PASSWORD);

        // Дополнительные настройки
        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        return mailSender;
    }
}
