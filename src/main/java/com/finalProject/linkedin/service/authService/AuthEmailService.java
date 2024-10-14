package com.finalProject.linkedin.service.authService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class AuthEmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public AuthEmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendConfirmationEmail(String to, String token) {
        String subject = "Подтверждение регистрации";
        String message = "<p>Для подтверждения вашего аккаунта нажмите на ссылку ниже:</p>" +
                "<a href=\"" + token + "\">Подтвердить аккаунт</a>";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject(subject);
        email.setText(message);
        mailSender.send(email);
    }

    public void sendResetEmail(String to, String token) {
        String subject = "Сброс пароля";
        String message = "Нажмите на ссылку для сброса пароля: " + token;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject(subject);
        email.setText(message);
        mailSender.send(email);

    }
}
