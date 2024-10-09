package com.Final_Project.Linkedin.controllers;

import com.Final_Project.Linkedin.dto.user.userReq.UserReq;
import com.Final_Project.Linkedin.entity.ConfirmationToken;
import com.Final_Project.Linkedin.entity.User;
import com.Final_Project.Linkedin.utils.enums.TokenType;
import com.Final_Project.Linkedin.password.PasswordValidator;
import com.Final_Project.Linkedin.services.authService.AuthEmailService;
import com.Final_Project.Linkedin.services.tokenService.ConfirmationTokenService;
import com.Final_Project.Linkedin.services.userService.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;


@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final AuthEmailService authEmailService;
    private final UserService userService;
    private ConfirmationTokenService confirmationTokenService;
    private final PasswordEncoder passwordEncoder;




    @PostMapping("/auth")
    public ResponseEntity<String> register(@RequestBody @Valid UserReq userRequest) {
        log.info("Registering with email: {}", userRequest.getEmail());
        if (userService.findUserByEmail(userRequest.getEmail()).isPresent()) {
            log.warn("User with email {} exists", userRequest.getEmail());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User exists");
        }

        log.info("Регистрация пользователя с паролем: {}", userRequest.getPassword().getPassword());

        User newUser = new User(
                userRequest.getEmail(),
                passwordEncoder.encode(userRequest.getPassword().getPassword())
        );
        log.info("Зашифрованный пароль: {}", newUser.getPassword());
        userService.save(newUser);
        log.info("Successfully registered with email: {}", userRequest.getEmail());

        String token = confirmationTokenService.createToken(newUser);
        String confirmationLink = "http://localhost:9000/api/confirm?token=" + token;
        authEmailService.sendConfirmationEmail(newUser.getEmail(), confirmationLink);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered. Check your email for confirmation.");
    }

    @GetMapping("/confirm")
    public String confirmAccount(@RequestParam("token") String token) {
        log.info("Confirming account with token: {}", token);
        Optional<ConfirmationToken> confirmationToken = confirmationTokenService.findByTokenAndTokenType(token, TokenType.REGISTRATION);

        if (confirmationToken.isPresent()) {
            log.info("Token found: {}", confirmationToken.get().getToken());
            if (confirmationToken.get().getConfirmedAt() != null) {
                return "Аккаунт уже подтвержден!";
            }

            LocalDateTime expiresAt = confirmationToken.get().getExpiresAt();
            if (expiresAt.isBefore(LocalDateTime.now())) {
                return "Срок действия токена истек!";
            }

            confirmationTokenService.setConfirmedAt(token, TokenType.REGISTRATION);

            User user = confirmationToken.get().getUser();
            user.setIsVerified(true);
            userService.save(user);
            return "Аккаунт успешно подтвержден! Можете закрыть страницу!";
        } else {
            return "Токен не найден!";
        }
    }

    @PostMapping("/password-forgot")
    public ResponseEntity<String> processForgotPassword(@RequestParam("email") String email) {
        log.warn("Email: " + email);
        Optional<User> user = userService.findUserByEmail(email);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        String token = confirmationTokenService.createPasswordResetTokenForUser(user.get());
        String confirmationLink = "http://localhost:3000/password-reset?token=" + token;
        authEmailService.sendResetEmail(user.get().getEmail(), confirmationLink);

        return ResponseEntity.ok("Письмо для сброса пароля отправлено");
    }

    @PostMapping("/password-reset")
    public ResponseEntity<String> resetPassword(@RequestParam("token") String token, @RequestBody PasswordValidator password) {

        log.warn("Attempting to reset password with token: {}", token);
        log.warn("Password: " + password.getPassword());

//        String actualPassword = "";
//        String[] parts = password.split(":");
//
//        // Проверяем, что строка содержит двоеточие и у нас есть хотя бы два элемента
//        if (parts.length == 2 && parts[0].equals("password")) {
//            actualPassword = parts[1];
//            log.warn("Extracted password: {}", actualPassword);
//        }

        ConfirmationToken resetToken = confirmationTokenService.findByTokenAndTokenType(token, TokenType.PASSWORD_RESET)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or expired token"));

        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token has expired");
        }

        User user = resetToken.getUser();
        log.info("Регистрация пользователя с паролем: {}", user.getPassword());
        user.setPassword(passwordEncoder.encode(password.getPassword()));
        log.info("Зашифрованный пароль: {}", user.getPassword());

        userService.save(user);


        // Удаляем токен после успешного сброса
        confirmationTokenService.deleteConfirmationToken(resetToken.getToken(), TokenType.PASSWORD_RESET);

        log.info("Password successfully reset for user: {}", user.getEmail());

        return ResponseEntity.ok("Password has been reset successfully");
    }

}
