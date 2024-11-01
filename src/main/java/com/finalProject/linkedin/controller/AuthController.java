package com.finalProject.linkedin.controller;

import com.finalProject.linkedin.dto.request.user.CreateUserReq;
import com.finalProject.linkedin.entity.ConfirmationToken;
import com.finalProject.linkedin.entity.User;
import com.finalProject.linkedin.utils.enums.TokenType;
import com.finalProject.linkedin.utils.password.PasswordValidator;
import com.finalProject.linkedin.service.serviceImpl.AuthEmailServiceImpl;
import com.finalProject.linkedin.service.serviceImpl.ConfirmationTokenServiceImpl;
import com.finalProject.linkedin.service.serviceImpl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;


@Slf4j
@RestController
@RequestMapping("/")
public class AuthController {

    private final AuthEmailServiceImpl authEmailServiceImpl;
    private final UserServiceImpl userServiceImpl;
    private final ConfirmationTokenServiceImpl confirmationTokenServiceImpl;
    private final PasswordEncoder passwordEncoder;
    private final String FRONT_URL;
    private final String APP_URL;

    public AuthController(
            AuthEmailServiceImpl authEmailServiceImpl,
            UserServiceImpl userServiceImpl,
            ConfirmationTokenServiceImpl confirmationTokenServiceImpl,
            PasswordEncoder passwordEncoder,
            @Value("${FRONT_URL}") String FRONT_URL,
            @Value("${APP_URL}") String APP_URL
    ) {
        this.authEmailServiceImpl = authEmailServiceImpl;
        this.userServiceImpl = userServiceImpl;
        this.confirmationTokenServiceImpl = confirmationTokenServiceImpl;
        this.passwordEncoder = passwordEncoder;
        this.FRONT_URL = FRONT_URL;
        this.APP_URL = APP_URL;
    }

    @PostMapping("/auth")
    public ResponseEntity<String> register(@RequestBody @Valid CreateUserReq createUserRequest) {

        if (userServiceImpl.findUserByEmail(createUserRequest.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User exists");
        }
        log.info("Реєстрація користувача з паролем: {}", createUserRequest.getPassword().getPassword());

        User newUser = new User(
                createUserRequest.getEmail(),
                passwordEncoder.encode(createUserRequest.getPassword().getPassword())
        );
        userServiceImpl.save(newUser);
        log.info("Успішно зареєстровано з електронною адресою: {}", createUserRequest.getEmail());

        String token = confirmationTokenServiceImpl.createToken(newUser);
        String confirmationLink = APP_URL + "/confirm?token=" + token;
        authEmailServiceImpl.sendConfirmationEmail(newUser.getEmail(), confirmationLink);
        return ResponseEntity.status(HttpStatus.CREATED).body("Користувач зареєстрований. Перевірте свою електронну пошту для підтвердження.");
    }

    @GetMapping("/confirm")
    public String confirmAccount(@RequestParam("token") String token) {
        log.info("Підтвердження облікового запису за допомогою токена: {}", token);
        Optional<ConfirmationToken> confirmationToken = confirmationTokenServiceImpl.findByTokenAndTokenType(token, TokenType.REGISTRATION);

        if (confirmationToken.isPresent()) {
            if (confirmationToken.get().getConfirmedAt() != null) {
                return "Акаунт вже підтверджено!";
            }

            LocalDateTime expiresAt = confirmationToken.get().getExpiresAt();
            if (expiresAt.isBefore(LocalDateTime.now())) {
                return "Термін дії токена минув!";
            }

            confirmationTokenServiceImpl.setConfirmedAt(token, TokenType.REGISTRATION);

            User user = confirmationToken.get().getUser();
            user.setIsVerified(true);
            userServiceImpl.save(user);
            return "Акаунт успішно підтверджено! Можете закрити сторінку!";
        } else {
            return "Токена не знайдено!";
        }
    }

    @PostMapping("/password-forgot")
    public ResponseEntity<String> processForgotPassword(@RequestParam("email") String email) {

        log.warn("Імейл: " + email);
        Optional<User> user = userServiceImpl.findUserByEmail(email);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        String token = confirmationTokenServiceImpl.createPasswordResetTokenForUser(user.get());
        String confirmationLink = FRONT_URL + "/password-reset?token=" + token;
        authEmailServiceImpl.sendResetEmail(user.get().getEmail(), confirmationLink);

        return ResponseEntity.ok("Лист для скидання пароля надіслано");
    }

    @PostMapping("/password-reset")
    public ResponseEntity<String> resetPassword(@RequestParam("token") String token, @RequestBody PasswordValidator password) {

        log.warn("Спроба скинути пароль за допомогою маркера: {}", token);
        log.warn("Пароль: " + password.getPassword());

        ConfirmationToken resetToken = confirmationTokenServiceImpl.findByTokenAndTokenType(token, TokenType.PASSWORD_RESET)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or expired token"));

        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token has expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(password.getPassword()));
        userServiceImpl.save(user);

        confirmationTokenServiceImpl.deleteConfirmationToken(resetToken.getToken(), TokenType.PASSWORD_RESET);

        log.info("Пароль успішно скинуто для користувача: {}", user.getEmail());

        return ResponseEntity.ok("Пароль успішно скинуто");
    }

}
