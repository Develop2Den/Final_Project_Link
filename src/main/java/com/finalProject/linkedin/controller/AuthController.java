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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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

//    @PostMapping("/auth")
//    @Operation(summary = "Register new user", description = "Registers a new user and sends confirmation email")
//    @ApiResponses({
//            @ApiResponse(responseCode = "201", description = "User registered successfully"),
//            @ApiResponse(responseCode = "400", description = "User already exists")
//    })
//    public ResponseEntity<String> register(@RequestBody @Valid CreateUserReq createUserRequest) {
//        if (userServiceImpl.findUserByEmail(createUserRequest.getEmail()).isPresent()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User exists");
//        }
//        log.info("Registering user with password: {}", createUserRequest.getPassword().getPassword());
//
//        User newUser = new User(
//                createUserRequest.getEmail(),
//                passwordEncoder.encode(createUserRequest.getPassword().getPassword())
//        );
//        userServiceImpl.save(newUser);
//        log.info("User registered successfully with email: {}", createUserRequest.getEmail());
//
//        String token = confirmationTokenServiceImpl.createToken(newUser);
//        String confirmationLink = APP_URL + "/confirm?token=" + token;
//        authEmailServiceImpl.sendConfirmationEmail(newUser.getEmail(), confirmationLink);
//        return ResponseEntity.status(HttpStatus.CREATED).body("User registered. Check your email for confirmation.");
//    }

    @PostMapping("/auth")
    @Operation(summary = "Register new user", description = "Registers a new user and sends confirmation email")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "User already exists"),
            @ApiResponse(responseCode = "409", description = "Incomplete registration")
    })
    public ResponseEntity<String> register(@RequestBody @Valid CreateUserReq createUserRequest) {
        // Проверяем, существует ли пользователь с таким email
        Optional<User> existingUser = userServiceImpl.findUserByEmail(createUserRequest.getEmail());

        if (existingUser.isPresent()) {
            User user = existingUser.get();

            // Если пользователь уже верифицирован
            if (user.getIsVerified()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("User exists");
            }

            // Если пользователь еще не завершил регистрацию
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("You haven't completed registration! Please check your email for confirmation.");
        }

        // Логирование пароля убрано, так как это небезопасно
        log.info("Registering user with email: {}", createUserRequest.getEmail());

        // Создание нового пользователя
        User newUser = new User(
                createUserRequest.getEmail(),
                passwordEncoder.encode(createUserRequest.getPassword().getPassword())
        );
        userServiceImpl.save(newUser);

        // Создание токена подтверждения
        String token = confirmationTokenServiceImpl.createToken(newUser);
        String confirmationLink = APP_URL + "/confirm?token=" + token;

        // Отправка email с подтверждением
        authEmailServiceImpl.sendConfirmationEmail(newUser.getEmail(), confirmationLink);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("User registered. Check your email for confirmation.");
    }


    @GetMapping("/confirm")
    @Operation(summary = "Confirm account", description = "Confirms the user's account using the token sent in the confirmation email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Account confirmed successfully"),
            @ApiResponse(responseCode = "400", description = "Token not found or expired")
    })
    public String confirmAccount(@RequestParam("token") String token) {
        log.info("Confirming account with token: {}", token);
        Optional<ConfirmationToken> confirmationToken = confirmationTokenServiceImpl.findByTokenAndTokenType(token, TokenType.REGISTRATION);

        if (confirmationToken.isPresent()) {
            if (confirmationToken.get().getConfirmedAt() != null) {
                return "Account already confirmed!";
            }

            LocalDateTime expiresAt = confirmationToken.get().getExpiresAt();
            if (expiresAt.isBefore(LocalDateTime.now())) {
                return "Token has expired!";
            }

            confirmationTokenServiceImpl.setConfirmedAt(token, TokenType.REGISTRATION);

            User user = confirmationToken.get().getUser();
            user.setIsVerified(true);
            userServiceImpl.save(user);
            return "Account confirmed successfully! You may close this page.";
        } else {
            return "Token not found!";
        }
    }

    @PostMapping("/password-forgot")
    @Operation(summary = "Forgot password", description = "Sends a password reset email to the user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password reset email sent"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<String> processForgotPassword(@RequestParam("email") String email) {
        log.warn("Email: " + email);
        Optional<User> user = userServiceImpl.findUserByEmail(email);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        String token = confirmationTokenServiceImpl.createPasswordResetTokenForUser(user.get());
        String confirmationLink = FRONT_URL + "/password-reset?token=" + token;
        authEmailServiceImpl.sendResetEmail(user.get().getEmail(), confirmationLink);

        return ResponseEntity.ok("Password reset email sent");
    }

    @PostMapping("/password-reset")
    @Operation(summary = "Reset password", description = "Resets the user's password using the provided token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password reset successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or expired token")
    })
    public ResponseEntity<String> resetPassword(@RequestParam("token") String token, @RequestBody PasswordValidator password) {
        log.warn("Attempting password reset with token: {}", token);
        log.warn("Password: " + password.getPassword());

        ConfirmationToken resetToken = confirmationTokenServiceImpl.findByTokenAndTokenType(token, TokenType.PASSWORD_RESET)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or expired token"));

        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token has expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(password.getPassword()));
        userServiceImpl.save(user);

        confirmationTokenServiceImpl.deleteConfirmationToken(resetToken.getToken(), TokenType.PASSWORD_RESET);

        log.info("Password successfully reset for user: {}", user.getEmail());

        return ResponseEntity.ok("Password reset successfully");
    }

}
