package com.Final_Project.Linkedin.controllers;

import com.Final_Project.Linkedin.dto.user.userReq.UserReq;
import com.Final_Project.Linkedin.entity.ConfirmationToken;
import com.Final_Project.Linkedin.entity.User;
import com.Final_Project.Linkedin.utils.enums.TokenType;
import com.Final_Project.Linkedin.utils.password.PasswordValidator;
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
        if (userService.findUserByEmail(userRequest.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User exists");
        }
        log.info("Реєстрація користувача з паролем: {}", userRequest.getPassword().getPassword());

        User newUser = new User(
                userRequest.getEmail(),
                passwordEncoder.encode(userRequest.getPassword().getPassword())
        );
        userService.save(newUser);
        log.info("Успішно зареєстровано з електронною адресою: {}", userRequest.getEmail());

        String token = confirmationTokenService.createToken(newUser);
        String confirmationLink = "http://localhost:9000/api/confirm?token=" + token;
        authEmailService.sendConfirmationEmail(newUser.getEmail(), confirmationLink);
        return ResponseEntity.status(HttpStatus.CREATED).body("Користувач зареєстрований. Перевірте свою електронну пошту для підтвердження.");
    }

    @GetMapping("/confirm")
    public String confirmAccount(@RequestParam("token") String token) {
        log.info("Підтвердження облікового запису за допомогою токена: {}", token);
        Optional<ConfirmationToken> confirmationToken = confirmationTokenService.findByTokenAndTokenType(token, TokenType.REGISTRATION);

        if (confirmationToken.isPresent()) {
            if (confirmationToken.get().getConfirmedAt() != null) {
                return "Акаунт вже підтверджено!";
            }

            LocalDateTime expiresAt = confirmationToken.get().getExpiresAt();
            if (expiresAt.isBefore(LocalDateTime.now())) {
                return "Термін дії токена минув!";
            }

            confirmationTokenService.setConfirmedAt(token, TokenType.REGISTRATION);

            User user = confirmationToken.get().getUser();
            user.setIsVerified(true);
            userService.save(user);
            return "Акаунт успішно підтверджено! Можете закрити сторінку!";
        } else {
            return "Токена не знайдено!";
        }
    }

    @PostMapping("/password-forgot")
    public ResponseEntity<String> processForgotPassword(@RequestParam("email") String email) {
        log.warn("Імейл: " + email);
        Optional<User> user = userService.findUserByEmail(email);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        String token = confirmationTokenService.createPasswordResetTokenForUser(user.get());
        String confirmationLink = "http://localhost:3000/password-reset?token=" + token;
        authEmailService.sendResetEmail(user.get().getEmail(), confirmationLink);

        return ResponseEntity.ok("Лист для скидання пароля надіслано");
    }

    @PostMapping("/password-reset")
    public ResponseEntity<String> resetPassword(@RequestParam("token") String token, @RequestBody PasswordValidator password) {

        log.warn("Спроба скинути пароль за допомогою маркера: {}", token);
        log.warn("Пароль: " + password.getPassword());

        ConfirmationToken resetToken = confirmationTokenService.findByTokenAndTokenType(token, TokenType.PASSWORD_RESET)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or expired token"));

        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token has expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(password.getPassword()));
        userService.save(user);

        confirmationTokenService.deleteConfirmationToken(resetToken.getToken(), TokenType.PASSWORD_RESET);

        log.info("Пароль успішно скинуто для користувача: {}", user.getEmail());

        return ResponseEntity.ok("Пароль успішно скинуто");
    }

}
